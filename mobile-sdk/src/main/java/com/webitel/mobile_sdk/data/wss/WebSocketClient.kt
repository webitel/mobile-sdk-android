package com.webitel.mobile_sdk.data.wss

import android.os.Handler
import android.os.HandlerThread
import com.webitel.mobile_sdk.data.auth.AccessToken
import com.webitel.mobile_sdk.data.auth.LoginResponse
import com.webitel.mobile_sdk.data.grps.AuthApi
import com.webitel.mobile_sdk.data.grps.ChannelConfig
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.grps.ConnectionListeners
import com.webitel.mobile_sdk.data.grps.GrpcListener
import com.webitel.mobile_sdk.data.grps.GrpcListeners
import com.webitel.mobile_sdk.data.portal.UserSession
import com.webitel.mobile_sdk.data.portal.WLogger
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.LogLevel
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.RegisterResult
import com.webitel.mobile_sdk.domain.User
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import okhttp3.CertificatePinner
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import webitel.chat.MessageOuterClass
import webitel.portal.Account
import webitel.portal.Auth
import webitel.portal.Auth.TokenRequest
import webitel.portal.Connect
import webitel.portal.Connect.Update
import webitel.portal.Connect.UpdateDisconnect
import webitel.portal.CustomerOuterClass
import webitel.portal.Media
import webitel.portal.Messages
import webitel.portal.Push
import java.util.concurrent.TimeUnit
import kotlin.onFailure
import kotlin.onSuccess


internal class WebSocketClient(
    private val config: ChannelConfig,
    private val logger: WLogger
) : WebSocketListener(), ChatApi, AuthApi, HttpClientProvider {
    private val pendingRequests = mutableListOf<Connect.Request>()
    private val workerThread = HandlerThread("WebSocketWorker").apply { start() }
    private val workerHandler = Handler(workerThread.looper)
    private var connectState: ConnectState = ConnectState.None
    private val headerProvider: HeaderProvider = HeaderProvider(config)

    private val connectionStateListeners = ConnectionListeners()
    private val connectionListeners = GrpcListeners()

    private var socket: WebSocket? = null

    private companion object {
        const val TAG = "WebSocketClient"
        const val WS_PATH = "/portal/ws"
        const val TOKEN_PATH = "/api/portal/token"
        const val LOGOUT_PATH = "/api/portal/logout"
        const val PUSH_REGISTER_PATH = "/api/portal/device"
    }


    val client: OkHttpClient

    init {
        val clientBuilder = OkHttpClient.Builder()
            .pingInterval(config.keepAliveTimeout, TimeUnit.SECONDS)
            .callTimeout(config.keepAliveTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(HeaderInterceptor(headerProvider))

        applyCertificatePinning(clientBuilder)

        client = clientBuilder.build()
    }


    override fun sendMessage(request: Connect.Request) {
        runJob {
            logger.debug(TAG, "sendMessage: $request")
            val s = socket
            if (s == null || connectState != ConnectState.Ready) {
                enqueueRequest(request)
                openStream()
                return@runJob
            }
            val json = ProtoJson.toJson(request)
            val sent = s.send(json)
            if (!sent) {
                logger.error(TAG,
                    "sendMessage: message was not sent, closing connection, requestId=${request.id}"
                )
                closeConnection()
                return@runJob
            }
            logger.debug(TAG, "sendMessage: sent requestId=${request.id}")
        }
    }


    override fun isStateReady(requestConnection: Boolean): Boolean {
        return connectState == ConnectState.Ready
    }


    override fun isStreamOpened(): Boolean {
        return connectState == ConnectState.Ready
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        logger.debug(TAG, "onOpen: ✅ Connected to WebSocket server")
        val oldState = connectState
        connectState = ConnectState.Ready
        flushPendingRequests(webSocket)
        connectionStateListeners.onStateChanged(from = oldState, to = ConnectState.Ready)
        connectionListeners.onConnectionReady()
    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            logger.debug(TAG, "onMessage: Received $text")
            val builder = Update.newBuilder()
            val update = ProtoJson.fromJson(text, builder)
            parseUpdate(update)
        } catch (e: Exception) {
            logger.error(TAG, "onMessage: error $e")
        }
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        logger.warn(TAG, "onFailure: ${t}, $response")

        val httpCode = response?.code

        val httpBody = response?.body?.let {
            try { it.string() } catch (_: Exception) { null }
        }

        val isCertError =
            t is javax.net.ssl.SSLPeerUnverifiedException ||
                    t.cause is javax.net.ssl.SSLPeerUnverifiedException ||
                    (t.message?.contains("Certificate pinning", ignoreCase = true) == true)

        val errorMessage = when {
            httpCode != null ->
                "WebSocket HTTP error: $httpCode $httpBody"

            isCertError && logger.level != LogLevel.DEBUG ->
                "WebSocket TLS error: certificate verification failed"

            t.message != null ->
                "WebSocket error: ${t.message}"

            else ->
                "Unknown WebSocket error"
        }

        val reason = Error(
            message = errorMessage,
            code = getCode(httpCode ?: -1)
        )

        val state = ConnectState.Disconnected(reason)
        logger.debug(TAG,
            "onFailure: onStateChanged - from = ${connectState}, to = $state"
        )

        val oldState = connectState
        connectState = state
        connectionListeners.onConnectionError(reason)
        connectionStateListeners.onStateChanged(from = oldState, to = state)
        pendingRequests.clear()
    }


    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        logger.warn(TAG, "onClosed: $code, $reason")
        val statusCode = Code.forNumber(code)
        val reason = Error(
            message = reason,
            code = statusCode
        )
        val state = ConnectState.Disconnected(reason)
        logger.debug(TAG,
            "onClosed: onStateChanged - from = ${connectState}, to = $state"
        )
        val oldState = connectState
        connectState = state
        connectionListeners.onConnectionError(reason)
        connectionStateListeners.onStateChanged(from = oldState, to = state)
        pendingRequests.clear()
    }


    override fun openConnection() {
        runJob {
            logger.debug(TAG, "openConnection requested by client, state=$connectState")
            openStream()
        }
    }


    override fun closeConnection() {
        try {
            logger.debug(TAG, "Calling closeConnection()")
            socket?.close(1000, "Client closed")
        } catch (e: Exception) {
            logger.error(TAG, "closeConnection: error" + e.message.toString())
        }
    }


    override fun ping(callback: CallbackListener<Unit>) {}


    override fun getConnectState(): ConnectState {
        return connectState
    }


    override fun addConnectListener(listener: ConnectListener) {
        connectionStateListeners.addListener(listener)
    }


    override fun removeConnectListener(listener: ConnectListener) {
        connectionStateListeners.removeListener(listener)
    }


    override fun addListener(listener: GrpcListener) {
        connectionListeners.addListener(listener)
    }


    override fun removeListener(listener: GrpcListener) {
        connectionListeners.addListener(listener)
    }


    override fun removeAllListeners() {
        connectionListeners.removeAllListeners()
    }


    override fun uploadFile(
        streamObserver: StreamObserver<MessageOuterClass.File>
    ): StreamObserver<Media.UploadMedia> {TODO("Not yet implemented") }


    override fun uploadFile(
        responseObserver: ClientResponseObserver<Media.UploadRequest, Media.UploadProgress>
    ): StreamObserver<Media.UploadRequest> {TODO("Not yet implemented")}


    override fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: ClientResponseObserver<Media.GetFileRequest, Media.MediaFile>
    ) {}


    override fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: StreamObserver<Media.MediaFile>
    ) {}


    override fun login(
        appToken: String,
        identity: Account.Identity,
        callback: CallbackListener<LoginResponse>
    ) {
        runJob {
            val request = TokenRequest.newBuilder()
                .setGrantType("identity")
                .addResponseType("user")
                .addResponseType("token")
                .addResponseType("chat")
                .setAppToken(appToken)
                .setIdentity(identity)
                .build()

            userLogin(request)
                .onSuccess(callback::onSuccess)
                .onFailure { callback.onError(it as Error) }
        }
    }


    override fun logout(callback: CallbackListener<Unit>) {
        runJob {
            val result = userLogout()
            result.onSuccess { it ->
                try {
                    socket?.close(1000, "Client closed")
                } catch (_: Exception) {}
                callback.onSuccess(it)
            }
            result.onFailure { it ->
                callback.onError(it as Error)
            }
        }
    }


    override fun inspect(callback: CallbackListener<UserSession>) {
        runJob {
            inspectUnaryRequest(callback)
        }
    }


    override fun setSession(
        auth: String,
        callback: CallbackListener<UserSession>
    ) {
        setAccessToken(auth)
        runJob {
            inspectUnaryRequest(object : CallbackListener<UserSession> {
                override fun onSuccess(t: UserSession) {
                    if (connectState == ConnectState.Ready) {

                        logger.debug(TAG, "setSession: refresh headers")
                        try {
                            socket?.close(1000, "refresh headers")
                        } catch (_: Exception) {}
                        val request = buildWebSocketRequest()
                        logger.debug(TAG, "refresh stream - $request")
                        socket = client.newWebSocket(request, this@WebSocketClient)
                        callback.onSuccess(t)

                    } else {
                        callback.onSuccess(t)
                    }
                }

                override fun onError(e: Error) {
                    callback.onError(e)
                }
            })
        }
    }


    override fun setAccessTokenHeader(
        auth: String,
        callback: CallbackListener<Unit>?
    ) {
        logger.debug(TAG, "Calling setAccessTokenHeader()")
        setAccessToken(auth)
        if (connectState == ConnectState.Ready) {
            runJob {
                logger.debug(TAG, "setAccessTokenHeader: refresh headers")
                try {
                    socket?.close(1000, "refresh headers")
                } catch (_: Exception) {}

                val request = buildWebSocketRequest()
                logger.debug(TAG, "refresh stream - $request")
                socket = client.newWebSocket(request, this)
                callback?.onSuccess(Unit)
            }
        } else {
            logger.debug(TAG, "setAccessTokenHeader: updated headers")
            callback?.onSuccess(Unit)
        }
    }


    override fun registerFcm(
        token: String,
        callback: CallbackListener<RegisterResult>
    ) {
        runJob {
            registerFcm(token)
                .onSuccess(callback::onSuccess)
                .onFailure { callback.onError(it as Error) }
        }
    }


    private fun openStream() {
        if (connectState == ConnectState.Ready || connectState == ConnectState.Connecting) {
            logger.warn(TAG, "openStream: already $connectState")
            return
        }
        val oldState = connectState
        connectState = ConnectState.Connecting
        connectionStateListeners.onStateChanged(from = oldState, to = ConnectState.Connecting)
        val request = buildWebSocketRequest()
        logger.debug(TAG, "open stream - $request")
        socket = client.newWebSocket(request, this)
    }


    private fun buildWebSocketRequest(): Request {
        val portPart = if (config.port > 0) ":${config.port}" else ""
        val url = "wss://${config.host}$portPart$WS_PATH"

        val builder = Request.Builder()
            .url(url)

        headerProvider.commonHeaders().forEach { (k, v) ->
            builder.addHeader(k, v)
        }

        return builder.build()
    }


    private fun inspect(): Result<UserSession> {
        return safeCall {
            logger.debug(TAG, "Calling inspect()")

            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(TOKEN_PATH)
                .build()

            val httpRequest = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorMessage = response.body?.string()
                    logger.error(TAG, "inspect: ${response.code}; $errorMessage")
                    throw Error("HTTP error $errorMessage", getCode(response.code))
                }

                val bodyString = response.body?.string() ?: run {
                    logger.error(TAG, "Empty response body. Code=${response.code}")
                    throw Error("Empty response body", Code.UNKNOWN)
                }
                logger.debug(TAG, "inspect: $bodyString")
                return@safeCall buildUserSession(bodyString)
            }
        }
    }


    private fun userLogin(request: TokenRequest): Result<LoginResponse> {
        return safeCall {
            val json = ProtoJson.toJson(request)
            val body = json.toRequestBody("application/json".toMediaType())

            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(TOKEN_PATH)
                .build()

            val httpRequest = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorMessage = response.body?.string()
                    logger.error(TAG, "userLogin: ${response.code} $errorMessage")
                    throw Error("HTTP error $errorMessage", getCode(response.code))
                }

                val bodyString = response.body?.string() ?: run {
                    logger.error(TAG, "userLogin: Empty response body. Code=${response.code}")
                    throw Error("Empty response body", Code.UNKNOWN)
                }
                logger.debug(TAG, "userLogin: $bodyString")
                val resp = buildLoginResponse(bodyString)
                headerProvider.updateAccessToken(resp.token.token)

                return@safeCall resp
            }
        }
    }


    private fun userLogout(): Result<Unit> {
        return safeCall {
            logger.debug(TAG, "Calling logout()")
            val request = CustomerOuterClass.LogoutRequest
                .newBuilder()
                .build()

            val json = ProtoJson.toJson(request)
            val body = json.toRequestBody("application/json".toMediaType())

            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(LOGOUT_PATH)
                .build()

            val httpRequest = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorMessage = response.body?.string()
                    logger.error(TAG, "logout: ${response.code} $errorMessage")
                    throw Error("HTTP error ${response.code} $errorMessage", getCode(response.code))
                }

                logger.debug(TAG, "logout: success")
                return@safeCall
            }
        }
    }


    private fun buildUserSession(jsonString: String): UserSession {
        val builder = Auth.AccessToken.newBuilder()
        ProtoJson.fromJson(jsonString, builder)
        val user = User.Builder(
            iss = builder.user.identity.iss,
            sub = builder.user.identity.sub,
            name = builder.user.identity.name
        ).build()

        val chatUser = Member(
            id = builder.chat.user.id,
            name = builder.chat.user.name,
            type = builder.chat.user.type
        )
        val session = UserSession(
            user = user,
            isChatAvailable = builder.scopeList.contains("chat"),
            chatAccount = chatUser,
        )
        logger.debug(TAG, "buildUserSession: $session")
        return session
    }


    private fun buildLoginResponse(jsonString: String): LoginResponse {
        val builder = Auth.AccessToken.newBuilder()
        ProtoJson.fromJson(jsonString, builder)
        val user = User.Builder(
            iss = builder.user.identity.iss,
            sub = builder.user.identity.sub,
            name = builder.user.identity.name
        ).build()

        val chatUser = Member(
            id = builder.chat.user.id,
            name = builder.chat.user.name,
            type = builder.chat.user.type
        )
        val response = LoginResponse(
            token = AccessToken(builder.accessToken, builder.expiresIn),
            UserSession(
                user = user,
                isChatAvailable = true,
                chatAccount = chatUser)
        )
        return response
    }


    inline fun <T> safeCall(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Error) {
            Result.failure(e)
        } catch (e: Exception) {
            val err = Error(message = e.message ?: "Unknown error", code = Code.UNKNOWN)
            logger.error("WebSocketClient", "safeCall: $err")
            Result.failure(err)
        }
    }


    private fun flushPendingRequests(webSocket: WebSocket) {
        val iterator = pendingRequests.iterator()
        while (iterator.hasNext()) {
            val req = iterator.next()
            val json = ProtoJson.toJson(req)
            val sent = webSocket.send(json)
            logger.debug(TAG, "flushPendingRequests: ${req.id}, sent - $sent")
            iterator.remove()
        }
    }


    private fun parseUpdate(update: Update) {
        if (update.data.`is`(Connect.Response::class.java)) {
            val response = update.data.unpack(Connect.Response::class.java)
            logger.debug(TAG, "parseUpdate: Response - $response")
            response?.let {
                connectionListeners.onResponse(it)
            }

        } else if (update.data.`is`(Messages.UpdateNewMessage::class.java)) {
            val message = update.data.unpack(Messages.UpdateNewMessage::class.java)
            logger.debug(TAG, "parseUpdate: UpdateNewMessage - $message")
            message?.let {
                connectionListeners.onNewMessage(it)
            }

        } else if (update.data.`is`(UpdateDisconnect::class.java)) {
            val disconnect = update.data.unpack(UpdateDisconnect::class.java)
            logger.debug(
                TAG,
                "UpdateDisconnect: server closes stream. " +
                        "code=${disconnect.cause.code}, message=${disconnect.cause.message}"
            )

            try {
                socket?.close(
                    safeCloseCode(disconnect.cause.code),
                    disconnect.cause.message
                )
            } catch (_: Exception) { }

        } else {
            logger.debug(TAG, "parseUpdate: notImplementedEvent - $update")
        }
    }


    private fun safeCloseCode(code: Int): Int =
        when (code) {
            1004, 1005, 1006, 1015 -> 1000 // fallback
            else -> code
        }


    private fun inspectUnaryRequest(callback: CallbackListener<UserSession>) {
        try {
            inspect()
                .onSuccess(callback::onSuccess)
                .onFailure { callback.onError(it as Error) }

        } catch (e: Exception) {
            logger.error(TAG, "inspectUnaryRequest: Exception - ${e.message}")
            callback.onError(
                Error(
                    e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    private fun getCode(httpCode: Int) : Code {
        return when (httpCode) {
            401 -> Code.UNAUTHENTICATED
            400 -> Code.FAILED_PRECONDITION
            500 -> Code.INTERNAL
            else -> Code.UNKNOWN
        }
    }


    private fun enqueueRequest(request: Connect.Request) {
        logger.debug(TAG, "enqueueRequest: $request")
        pendingRequests.add(request)
    }


    private fun setAccessToken(auth: String) {
        headerProvider.updateAccessToken(auth)
    }


    private fun registerFcm(token: String): Result<RegisterResult> {
        return safeCall {
            logger.debug(TAG, "Calling registerFcm()")
            val device = Push.DevicePush.newBuilder().setFCM(token).build()
            val request = CustomerOuterClass.RegisterDeviceRequest
                .newBuilder()
                .setPush(device)
                .build()

            val json = ProtoJson.toJson(request)
            val body = json.toRequestBody("application/json".toMediaType())

            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(PUSH_REGISTER_PATH)
                .build()

            val httpRequest = Request.Builder()
                .url(url)
                .post(body)
                .build()

            logger.debug(TAG, "registerFcm: send $httpRequest; $json")
            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    val httpCode = response.code

                    val httpBody = response.body?.let {
                        try { it.string() } catch (_: Exception) { null }
                    }

                    val reason = Error(
                        message = "WebSocket HTTP error: $httpCode $httpBody",
                        code = getCode(httpCode)
                    )
                    logger.error(TAG, "registerFcm: ${response.code} ${response.message}")
                    throw reason
                }

                logger.debug(TAG, "registerFcm: success")
                return@safeCall RegisterResult()
            }
        }
    }


    private fun applyCertificatePinning(builder: OkHttpClient.Builder) {
        if (config.pinnedHashes.isEmpty()) return

        val normalizedPins = config.pinnedHashes
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { normalizeSha256Pin(it) }
            .toList()

        if (normalizedPins.isEmpty()) return

        val certificatePinner = CertificatePinner.Builder()
            .add(config.host, *normalizedPins.toTypedArray())
            .build()

        builder.certificatePinner(certificatePinner)
    }


    private fun normalizeSha256Pin(pin: String): String {
        return if (pin.startsWith("sha256/")) {
            pin
        } else {
            "sha256/$pin"
        }
    }


    private fun runJob(job: () -> Unit) {
        workerHandler.post(job)
    }


    override fun client(): OkHttpClient {
        return client
    }
}