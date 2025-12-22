package com.webitel.mobile_sdk.data.portal

import com.webitel.mobile_sdk.data.auth.AuthRepository
import com.webitel.mobile_sdk.data.chats.WebitelChat
import com.webitel.mobile_sdk.data.grps.ChannelConfig
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.grps.ClientGrpc
import com.webitel.mobile_sdk.data.repository.DeviceInfoRepository
import com.webitel.mobile_sdk.data.repository.storage.DeviceInfoStorageSharedPref
import com.webitel.mobile_sdk.data.wss.WebSocketClient
import com.webitel.mobile_sdk.domain.ChatClient
import com.webitel.mobile_sdk.domain.User
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.LoginListener
import com.webitel.mobile_sdk.domain.PortalClient
import com.webitel.mobile_sdk.domain.RegisterResult
import com.webitel.mobile_sdk.domain.Session
import androidx.core.net.toUri
import com.webitel.mobile_sdk.data.chats.FileUploaderGRPC
import com.webitel.mobile_sdk.data.wss.FileDownloaderHttp
import com.webitel.mobile_sdk.data.wss.FileUploaderHttp
import com.webitel.mobile_sdk.data.wss.HttpClientProvider
import com.webitel.mobile_sdk.domain.Transport


internal class WebitelPortalClient(
    private val client: PortalClient.Builder
) : PortalClient {

    private val authRepository: AuthRepository
    private val deviceInfoRepository = DeviceInfoRepository(
        DeviceInfoStorageSharedPref(
            client.application
        )
    )

    private val chatApi: ChatApi
    private val chat: WebitelChat

    private var userSession: UserSession? = null

    companion object {
        val logger: WLogger = WLogger()
    }


    override val chatClient: ChatClient
        get() {
            return chat
        }


    init {
        logger.level = client.logLevel
        val config = getChannelConfig()

        val isWebSocket = client.transport == Transport.WEBSOCKET

        val api = if (isWebSocket)
            WebSocketClient(config, logger)
        else
            ClientGrpc(config, logger)

        val httpProvider = api as? HttpClientProvider

        val fileUploader = if (isWebSocket && httpProvider != null)
            FileUploaderHttp(config, httpProvider)
        else
            FileUploaderGRPC(api)

        val fileDownloaderHttp = if (isWebSocket && httpProvider != null)
            FileDownloaderHttp(config, httpProvider)
        else
            null

        chat = WebitelChat(api, logger, fileUploader, fileDownloaderHttp)
        authRepository = AuthRepository(api)

        api.addListener(chat)
        api.addListener(authRepository)
        chatApi = api
    }


    override fun getChatClient(callback: CallbackListener<ChatClient>) {
        authRepository.getSession(object : CallbackListener<UserSession> {
            override fun onSuccess(t: UserSession) {
                userSession = t
                if (t.isChatAvailable) callback.onSuccess(chat)
                else callback.onError(
                    Error(
                        "Chat is not enabled in this service",
                        Code.UNIMPLEMENTED
                    )
                )
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    override fun userLogin(user: User, callback: LoginListener) {
        authRepository.userLogin(client.token, user, object : CallbackListener<UserSession> {
            override fun onSuccess(t: UserSession) {
                userSession = t
                callback.onLoginFinished(t)
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    override fun userLogout(callback: LoginListener) {
        authRepository.logout(callback)
    }


    override fun getUserSession(callback: CallbackListener<Session>) {
        authRepository.getSession(object : CallbackListener<UserSession> {
            override fun onSuccess(t: UserSession) {
                userSession = t
                callback.onSuccess(t)
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    override fun setAccessToken(token: String, callback: CallbackListener<Session>) {
        authRepository.setAccessToken(token, object : CallbackListener<UserSession> {
            override fun onSuccess(t: UserSession) {
                userSession = t
                callback.onSuccess(t)
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    override fun setAccessTokenHeader(token: String) {
        authRepository.setAccessTokenHeader(token, null)
    }


    override fun setAccessTokenHeader(token: String, callback: CallbackListener<Unit>) {
        authRepository.setAccessTokenHeader(token, callback)
    }


    override fun registerDevice(pushToken: String, callback: CallbackListener<RegisterResult>) {
        authRepository.registerFcm(pushToken, callback)
    }


    override fun handleFCMNotification(data: Map<String, String>) {}


    override fun getConnectState(): ConnectState {
        return chatApi.getConnectState()
    }


    override fun addConnectListener(listener: ConnectListener) {
        chatApi.addConnectListener(listener)
    }


    override fun removeConnectListener(listener: ConnectListener) {
        chatApi.removeConnectListener(listener)
    }


    override fun openConnect() {
        chatApi.openConnection()
    }

    override fun closeConnect() {
        chatApi.closeConnection()
    }


    private fun getChannelConfig(): ChannelConfig {
        val uri = client.address.toUri()

        if (uri.host.isNullOrEmpty())
            throw Exception("Bad address - ${client.address}")

        val deviceId = client.deviceId.ifEmpty {
            deviceInfoRepository.getDeviceId()
        }

        return ChannelConfig(
            host = uri.host ?: "",
            port = uri.port,
            agent = getUserAgent(),
            clientToken = client.token,
            deviceId = deviceId,
            keepAliveTime = client.keepAliveTime,
            keepAliveTimeout = client.keepAliveTimeout,
            scheme = uri.scheme ?: "https",
            transport = client.transport
        )
    }


    private fun getUserAgent(): String {
        val appName: String = client.name.ifEmpty {
            client.application.packageName
        }
        val version: String = client.ver

        return deviceInfoRepository.getUserAgent(
            appName = appName,
            appVersion = version
        )
    }
}