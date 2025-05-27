package com.webitel.mobile_sdk.data.grps

import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import com.google.protobuf.Any
import com.google.protobuf.ByteString
import com.webitel.mobile_sdk.data.auth.AccessToken
import com.webitel.mobile_sdk.data.auth.LoginResponse
import com.webitel.mobile_sdk.data.portal.UserSession
import com.webitel.mobile_sdk.data.portal.WLogger
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.User
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.RegisterResult
import io.grpc.ConnectivityState
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import webitel.chat.MessageOuterClass
import webitel.portal.Account.Identity
import webitel.portal.Auth
import webitel.portal.Auth.TokenRequest
import webitel.portal.Connect.Echo
import webitel.portal.Connect.Request
import webitel.portal.Connect.Response
import webitel.portal.Connect.Update
import webitel.portal.Connect.UpdateDisconnect
import webitel.portal.CustomerGrpc
import webitel.portal.CustomerOuterClass
import webitel.portal.CustomerOuterClass.RegisterDeviceResponse
import webitel.portal.Media
import webitel.portal.MediaStorageGrpc
import webitel.portal.Messages
import webitel.portal.Push
import java.util.Timer
import java.util.TimerTask


internal class ClientGrpc(
    private val config: ChannelConfig,
    private val logger: WLogger
) : ChatApi, AuthApi, StreamListener {

    private var chatListener: GrpcChatMessageListener? = null
    private var requestObserver: StreamObserver<Request>? = null
    private var timer: Timer? = null
    private lateinit var channel: GrpcChannel
    private val connectionListeners = ConnectionListeners()

    private val grpcListeners = GrpcListeners()

    private val thread: HandlerThread = HandlerThread(
        "grpc_webitel",
        Process.THREAD_PRIORITY_BACKGROUND
    )

    private val handler: Handler by lazy {
        Handler(thread.looper)
    }

    private var connectState: ConnectState = ConnectState.None


    init {
        initChannel()
    }


    fun addConnectListener(listener: ConnectListener) {
        connectionListeners.addListener(listener)
    }


    fun removeConnectListener(listener: ConnectListener) {
        connectionListeners.removeListener(listener)
    }


    private fun initChannel() {
        channel = GrpcUtils.getChannel(config)
        channel.setStreamListener(this)
        val connectivityStateWatcher = object : Runnable {
            override fun run() {
                val state = channel.channel.getState(false)

                when (state) {
                    ConnectivityState.CONNECTING -> {
                        //Log.e("state", "CONNECTING")
                    }

                    ConnectivityState.SHUTDOWN -> {
                        //Log.e("state", "SHUTDOWN")
                        channel.setStreamListener(null)
                        initChannel()
                        return // DISCONNECTED state is final.
                    }

                    ConnectivityState.TRANSIENT_FAILURE -> {
                        //Log.e("state", "TRANSIENT_FAILURE")
                    }

                    ConnectivityState.READY -> {
                        //Log.e("state", "READY")
                        chatListener?.onConnectionReady()
                    }

                    ConnectivityState.IDLE -> {
                        //Log.e("state", "IDLE")
                    }

                    else -> {
                        //Log.e("state", "null")
                        // receive null
                    }
                }

                channel.channel.notifyWhenStateChanged(state, this)
            }
        }

        val state = channel.channel.getState(true)
        channel.channel.notifyWhenStateChanged(state, connectivityStateWatcher)
    }


    override fun inspect(
        callback: CallbackListener<UserSession>
    ) {
        make {
            inspectUnaryRequest(callback)
        }
    }


    override fun setSession(auth: String, callback: CallbackListener<UserSession>) {
        make {
            setSessionUnaryRequest(auth, callback)
        }
    }


    override fun setAccessTokenHeader(auth: String, callback: CallbackListener<Unit>?) {
        make {
            setAccessToken(auth)
            if (requestObserver != null) {
                try {
                    resetBackoff()
                    val stub = CustomerGrpc.newStub(channel.channel)
                    val m = CustomerOuterClass.InspectRequest
                        .newBuilder()
                        .build()

                    stub.inspect(m, object : StreamObserver<Auth.AccessToken> {
                        override fun onNext(value: Auth.AccessToken?) {
                            logger.debug("ClientGrpc",
                                "setAccessTokenHeader: token updated in headers and stream"
                            )
                            callback?.onSuccess(Unit)
                        }
                        override fun onError(t: Throwable) {
                            logger.error("ClientGrpc",
                                "setAccessTokenHeader: token not updated in stream. ${t.message}"
                            )
                            callback?.let {
                                val err = parseError(t)
                                it.onError(err)
                            }
                        }
                        override fun onCompleted() {}
                    })
                } catch (e: Exception) {
                    logger.error("ClientGrpc",
                        "setAccessTokenHeader: token not updated in stream. Exception - ${e.message}"
                    )
                    callback?.let {
                        val err = parseError(e)
                        it.onError(err)
                    }
                }

            }else {
                logger.debug("ClientGrpc",
                    "setAccessTokenHeader: token updated in headers"
                )
                callback?.onSuccess(Unit)
            }
        }
    }


    override fun registerFcm(token: String, callback: CallbackListener<RegisterResult>) {
        make {
            registerFcmUnaryRequest(token, callback)
        }
    }


    override fun login(
        appToken: String,
        identity: Identity,
        callback: CallbackListener<LoginResponse>
    ) {
        make {
            try {
                resetBackoff()
                val stub = CustomerGrpc.newStub(channel.channel)
                val m = TokenRequest
                    .newBuilder()
                    .setGrantType("identity")
                    .addResponseType("user")
                    .addResponseType("token")
                    .addResponseType("chat")
                    .setAppToken(appToken)
                    .setIdentity(identity)
                    .build()

                stub.token(m, object : StreamObserver<Auth.AccessToken> {

                    override fun onNext(value: Auth.AccessToken?) {
                        if (value != null) {
                            setAccessToken(value.accessToken)
                            val s = buildSessionFromResponse(value)

                            val t = AccessToken(
                                token = value.accessToken,
                                expiresIn = value.expiresIn
                            )
                            logger.debug("ClientGrpc",
                                "login: success"
                            )
                            callback.onSuccess(
                                LoginResponse(t,s)
                            )
                        } else {
                            logger.error("ClientGrpc",
                                "login: auth.AccessToken not found"
                            )
                            callback.onError(
                                Error(
                                    "Auth.AccessToken not found",
                                    code = Code.DATA_LOSS
                                )
                            )
                        }
                    }

                    override fun onError(t: Throwable) {
                        logger.error("ClientGrpc",
                            "login: onError - ${t.message.toString()}"
                        )
                        callback.onError(parseError(t))
                    }

                    override fun onCompleted() {}
                })

            } catch (e: Exception) {
                logger.error("ClientGrpc",
                    "login: Exception - ${e.message}"
                )
                callback.onError(parseError(e))
            }
        }
    }


    override fun logout(
        callback: CallbackListener<Unit>
    ) {
        make {
            try {
                resetBackoff()
                val stub = CustomerGrpc.newStub(channel.channel)
                val m = CustomerOuterClass.LogoutRequest
                    .newBuilder()
                    .build()

                stub.logout(m, object : StreamObserver<UpdateDisconnect> {
                    override fun onNext(value: UpdateDisconnect?) {
                        logger.debug("ClientGrpc",
                            "logout: success"
                        )
                        callback.onSuccess(Unit)
                    }

                    override fun onError(t: Throwable) {
                        logger.error("ClientGrpc",
                            "logout: onError - ${t.message.toString()}"
                        )
                        callback.onError(parseError(t))
                    }

                    override fun onCompleted() {}
                })

            } catch (e: Exception) {
                logger.error("ClientGrpc",
                    "logout: Exception - ${e.message}"
                )
                callback.onError(parseError(e))
            }
        }
    }


    override fun startPing() {
        make {
            startPinging()
        }
    }


    override fun stopPing() {
        make {
            stopPinging()
        }
    }


    override fun sendMessage(request: Request) {
        make {
            postData(request)
        }
    }


    override fun ping(callback: CallbackListener<Unit>) {
        make {
            try {
                resetBackoff()
                val stub = CustomerGrpc.newStub(channel.channel)
                val e = Echo
                    .newBuilder()
                    .build()

                stub.ping(e, object : StreamObserver<Echo> {
                    override fun onNext(value: Echo?) {
                        callback.onSuccess(Unit)
                    }

                    override fun onError(t: Throwable) {
                        callback.onError(parseError(t))
                    }

                    override fun onCompleted() {}
                })

            } catch (e: Exception) {
                callback.onError(parseError(e))
            }
        }
    }


    override fun isStreamOpened(): Boolean {
        val s = channel.channel.getState(true)
        val x = s == ConnectivityState.READY && requestObserver != null
        return x
    }


    override fun isStateReady(requestConnection: Boolean): Boolean {
        val s = channel.channel.getState(requestConnection)
        return s == ConnectivityState.READY
    }


    private fun resetBackoff() {
        val s = channel.channel.getState(true)
        if(s ==  ConnectivityState.TRANSIENT_FAILURE) {
            channel.channel.resetConnectBackoff()
        }
    }


    override fun uploadFile(
        streamObserver: StreamObserver<MessageOuterClass.File>
    ): StreamObserver<Media.UploadMedia> {
        val stub = MediaStorageGrpc.newStub(channel.channel)
        return stub.uploadFile(streamObserver)
    }


    override fun uploadFile(
        responseObserver: ClientResponseObserver<Media.UploadRequest, Media.UploadProgress>
    ): StreamObserver<Media.UploadRequest> {
        val stub = MediaStorageGrpc.newStub(channel.channel)
        return stub.upload(responseObserver)
    }


    override fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: StreamObserver<Media.MediaFile>
    ){
        val stub = MediaStorageGrpc.newStub(channel.channel)
        stub.getFile(request, streamObserver)
    }


    override fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: ClientResponseObserver<Media.GetFileRequest, Media.MediaFile>
    ) {
        val stub = MediaStorageGrpc.newStub(channel.channel)
        stub.getFile(request, streamObserver)
    }


    fun setAccessToken(t: String) {
        channel.setAccessToken(t)
    }


    fun setChatListener(l: GrpcChatMessageListener) {
        this.chatListener = l
    }


    fun addListener(l: GrpcListener) {
        this.grpcListeners.addListener(l)
    }


    fun removeListener(l: GrpcListener) {
        this.grpcListeners.removeListener(l)
    }


    fun removeAllListeners() {
        this.grpcListeners.removeAllListeners()
    }


    override fun openConnection() {
        val state = channel.channel.getState(true)
        if (state == ConnectivityState.TRANSIENT_FAILURE) {
            channel.channel.resetConnectBackoff()
            channel.channel.enterIdle()
            checkAndOpenConnection()
        } else {
            checkAndOpenConnection()
        }
    }


    override fun closeConnection() {
        stopStream()
    }


    @Synchronized
    private fun postData(request: Request) {
        checkAndOpenConnection()
        logger.debug("ClientGrpc",
            "postData: $request"
        )
        requestObserver?.onNext(request)
    }


    @Synchronized
    private fun checkAndOpenConnection() {
        if (requestObserver == null) {
            openBiDirectionalConnect()
        }
    }


    private fun stopStream() {
        requestObserver?.onCompleted()
        requestObserver = null
        timer?.cancel()
        timer = null
    }


    private fun registerFcmUnaryRequest(token: String, callback: CallbackListener<RegisterResult>) {
        try {
            resetBackoff()
            val stub = CustomerGrpc.newStub(channel.channel)
            val d = Push.DevicePush.newBuilder().setFCM(token).build()
            val i = CustomerOuterClass.RegisterDeviceRequest
                .newBuilder()
                .setPush(d)
                .build()

            stub.registerDevice(i, object : StreamObserver<RegisterDeviceResponse> {

                override fun onNext(value: RegisterDeviceResponse?) {
                    logger.debug("ClientGrpc",
                        "registerFcmUnaryRequest: success"
                    )
                    callback.onSuccess(RegisterResult())
                }

                override fun onError(t: Throwable) {
                    logger.error("ClientGrpc",
                        "registerFcmUnaryRequest: onError - ${t.message}"
                    )
                    callback.onError(parseError(t))
                }

                override fun onCompleted() {}
            })

        } catch (e: Exception) {
            logger.error("ClientGrpc",
                "registerFcmUnaryRequest: Exception - ${e.message}"
            )
            callback.onError(
                Error(
                    e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    private fun setSessionUnaryRequest(
        auth: String,
        callback: CallbackListener<UserSession>) {
        try {
            resetBackoff()
            setAccessToken(auth)
            val stub = CustomerGrpc.newStub(channel.channel)
            val m = CustomerOuterClass.InspectRequest
                .newBuilder()
                .build()

            stub.inspect(m, object : StreamObserver<Auth.AccessToken> {
                override fun onNext(value: Auth.AccessToken?) {
                    if (value != null) {
                        val s = buildSessionFromResponse(value)
                        logger.debug("ClientGrpc",
                            "setSessionUnaryRequest: success"
                        )
                        callback.onSuccess(s)
                    } else {
                        logger.error("ClientGrpc",
                            "setSessionUnaryRequest: Auth.AccessToken not found"
                        )
                        callback.onError(
                            Error(
                                "Auth.AccessToken not found",
                                code = Code.DATA_LOSS
                            )
                        )
                    }
                }

                override fun onError(t: Throwable) {
                    logger.error("ClientGrpc",
                        "setSessionUnaryRequest: onError - ${t.message}"
                    )
                    callback.onError(parseError(t))
                }

                override fun onCompleted() {}
            })

        } catch (e: Exception) {
            logger.error("ClientGrpc",
                "setSessionUnaryRequest: Exception - ${e.message}"
            )
            callback.onError(
                Error(
                    e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    private fun inspectUnaryRequest(callback: CallbackListener<UserSession>) {
        try {
            resetBackoff()
            val stub = CustomerGrpc.newStub(channel.channel)
            val m = CustomerOuterClass.InspectRequest
                .newBuilder()
                .build()

            stub.inspect(m, object : StreamObserver<Auth.AccessToken> {

                override fun onNext(value: Auth.AccessToken?) {
                    if (value != null) {
                        val s = buildSessionFromResponse(value)
                        logger.debug("ClientGrpc",
                            "inspectUnaryRequest: success"
                        )
                        callback.onSuccess(s)
                    } else {
                        logger.error("ClientGrpc",
                            "inspectUnaryRequest: Auth.AccessToken not found"
                        )
                        callback.onError(
                            Error(
                                "Auth.AccessToken not found",
                                code = Code.DATA_LOSS
                            )
                        )
                    }
                }

                override fun onError(t: Throwable) {
                    logger.error("ClientGrpc",
                        "inspectUnaryRequest: onError - ${t.message}"
                    )
                    callback.onError(parseError(t))
                }

                override fun onCompleted() {}
            })

        } catch (e: Exception) {
            logger.error("ClientGrpc",
                "inspectUnaryRequest: Exception - ${e.message}"
            )
            callback.onError(
                Error(
                    e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    private fun buildSessionFromResponse(value: Auth.AccessToken): UserSession {
        val chatAccount = if (value.chat != null && value.chat.user != null) {
            Member(
                id = value.chat.user.id,
                name = value.chat.user.name,
                type = value.chat.user.type
            )
        } else null

        val user = User.Builder(
            iss = value.user.identity.iss,
            sub = value.user.identity.sub,
            name = value.user.identity.name
        )
            .email(value.user.identity.email)
            .emailVerified(value.user.identity.emailVerified)
            .phoneNumber(value.user.identity.phoneNumber)
            .phoneNumberVerified(value.user.identity.phoneNumberVerified)
            .locale(value.user.identity.locale)
            .build()

        return UserSession(
            user = user,
            isChatAvailable = value.scopeList.contains("chat"),
            chatAccount
        )
    }


    private fun openBiDirectionalConnect() {
        try {
            resetBackoff()
            logger.debug("ClientGrpc",
                "openConnect: create new connection..."
            )
            val stub = CustomerGrpc.newStub(channel.channel)
            requestObserver?.onCompleted()
            requestObserver = stub.connect(object : StreamObserver<Update> {

                override fun onNext(value: Update?) {
                    if (value != null) {
                        parseUpdate(value)
                    }
                }

                override fun onError(t: Throwable) {
                    logger.error("ClientGrpc",
                        "openConnect: $t"
                    )
                    stopStream()

                    val e = parseError(t)

                    grpcListeners.onConnectionError(e)
                    chatListener?.onConnectionError(e)
                }


                override fun onCompleted() {
                    stopStream()
                }
            })
        } catch (e: Exception) {
            logger.error("ClientGrpc",
                "openConnect: Exception - ${e.message}"
            )
        }
    }


    private fun parseError(t: Throwable): Error {
        return if (t is StatusRuntimeException) {
            Error(
                message = t.status.description ?: t.message.toString(),
                code = Code.forNumber(t.status.code.value())
            )
        } else {
            Error(
                message = t.message.toString(),
                code = Code.UNKNOWN
            )
        }
    }


    private fun parseUpdate(update: Update) {
        if (update.data.`is`(Response::class.java)) {
            val response = update.data.unpack(Response::class.java)
            logger.debug("ClientGrpc",
                "parseUpdate: Response - $response"
            )
            response?.let {
                grpcListeners.onResponse(it)
                chatListener?.onResponse(it)
            }

        } else if (update.data.`is`(Messages.UpdateNewMessage::class.java)) {
            val message = update.data.unpack(Messages.UpdateNewMessage::class.java)
            logger.debug("ClientGrpc",
                "parseUpdate: UpdateNewMessage - $message"
            )
            message?.let {
                chatListener?.onNewMessage(it)
            }

        } else if (update.data.`is`(UpdateDisconnect::class.java)) {
            logger.debug("ClientGrpc",
                "parseUpdate: UpdateDisconnect - server closes connection..."
            )

        } else {
            logger.debug("ClientGrpc",
                "parseUpdate: notImplementedEvent - $update"
            )
        }
    }


    private fun startPinging() {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                ping()
            }
        }, 0, 2000)
    }


    private fun stopPinging() {
        timer?.cancel()
        timer = null
    }


    private var pingId = 0
    private fun ping() {
        pingId++
        val m = Echo
            .newBuilder()
            .setData(
                ByteString.copyFromUtf8("ping - $pingId")
            ).build()

        val request = Request.newBuilder()
            .setId(pingId.toString())
            .setPath(CustomerGrpc.getPingMethod().bareMethodName)
            .setData(Any.newBuilder().pack(m))
            .build()

        postData(request)
    }


    private fun make(job: Runnable) {
        if (!thread.isAlive) {
            thread.priority = Thread.MAX_PRIORITY
            thread.start()
        }

        handler.post(job)
    }


    fun getConnectState(): ConnectState {
        return connectState
    }


    override fun onStart(methodName: String) {
        if (methodName != CustomerGrpc.getConnectMethod().bareMethodName) return
        logger.debug("ClientGrpc",
            "onStart: onStateChanged - from = $connectState, to = ${ConnectState.Connecting}"
        )
        connectionListeners.onStateChanged(from = connectState, to = ConnectState.Connecting)
        connectState = ConnectState.Connecting
    }


    override fun onReady(methodName: String) {
        if (methodName != CustomerGrpc.getConnectMethod().bareMethodName) return
        logger.debug("ClientGrpc",
            "onReady: onStateChanged - from = $connectState, to = ${ConnectState.Ready}"
        )
        connectionListeners.onStateChanged(from = connectState, to = ConnectState.Ready)
        connectState = ConnectState.Ready
    }


    override fun onClose(methodName: String, status: Status?, trailers: Metadata?) {
        if (methodName != CustomerGrpc.getConnectMethod().bareMethodName) return
        val statusCode = Code.forNumber(status?.code?.value() ?: 2)
        val reason = Error(
            message = status?.description ?: status.toString(),
            code = statusCode
        )
        val state = ConnectState.Disconnected(reason)
        logger.debug("ClientGrpc",
            "onClose: onStateChanged - from = ${connectState}, to = $state"
        )
        connectionListeners.onStateChanged(from = connectState, to = state)
        connectState = state
    }
}


