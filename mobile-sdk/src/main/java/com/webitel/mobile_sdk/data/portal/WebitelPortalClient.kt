package com.webitel.mobile_sdk.data.portal

import android.net.Uri
import com.webitel.mobile_sdk.data.auth.AuthRepository
import com.webitel.mobile_sdk.data.auth.storage.AuthStorageSharedPref
import com.webitel.mobile_sdk.data.calls.WebitelVoice
import com.webitel.mobile_sdk.data.chats.WebitelChat
import com.webitel.mobile_sdk.data.grps.ChannelConfig
import com.webitel.mobile_sdk.data.grps.ClientGrpc
import com.webitel.mobile_sdk.data.repository.DeviceInfoRepository
import com.webitel.mobile_sdk.data.repository.storage.DeviceInfoStorageSharedPref
import com.webitel.mobile_sdk.domain.ChatClient
import com.webitel.mobile_sdk.domain.User
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.LoginListener
import com.webitel.mobile_sdk.domain.VoiceClient
import com.webitel.mobile_sdk.domain.PortalClient
import com.webitel.mobile_sdk.domain.RegisterResult
import com.webitel.mobile_sdk.domain.Session


internal class WebitelPortalClient(
    private val client: PortalClient.Builder
) : PortalClient {

    private val authRepository: AuthRepository
    private val deviceInfoRepository = DeviceInfoRepository(
        DeviceInfoStorageSharedPref(
            client.application
        )
    )

    private val grpc: ClientGrpc
    private val chat: WebitelChat
    private val voice: VoiceClient

    private var userSession: UserSession? = null
    private val logger: WLogger


    init {
        val session: () -> UserSession? = {
            userSession
        }

        logger = WLogger(client.logLevel)

        grpc = ClientGrpc(
            getChannelConfig(),
            logger
        )

        chat = WebitelChat(grpc, logger)
        voice = WebitelVoice(grpc)

        authRepository = AuthRepository(
            AuthStorageSharedPref(client.application),
            grpc
        )

        setupUser()

        grpc.setChatListener(chat)
        grpc.addListener(authRepository)
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


    override fun getVoiceClient(callback: CallbackListener<VoiceClient>) {

        if (voice.activeCall != null) {
            callback.onSuccess(voice)
            return
        }

        authRepository.getSession(object : CallbackListener<UserSession> {
            override fun onSuccess(t: UserSession) {
                userSession = t
                if (t.isVoiceAvailable) callback.onSuccess(voice)
                else callback.onError(
                    Error(
                        "Calls is not enabled in this service",
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
        authRepository.setAccessTokenHeader(token)
    }


    override fun registerDevice(pushToken: String, callback: CallbackListener<RegisterResult>) {
        authRepository.registerFcm(pushToken, callback)
    }


    override fun handleFCMNotification(data: Map<String, String>) {}


    override fun getConnectState(): ConnectState {
        return grpc.getConnectState()
    }


    override fun addConnectListener(listener: ConnectListener) {
        grpc.addConnectListener(listener)
    }


    override fun removeConnectListener(listener: ConnectListener) {
        grpc.removeConnectListener(listener)
    }


    override fun openConnect() {
        grpc.openConnection()
    }

    override fun closeConnect() {
        grpc.closeConnection()
    }


    private fun setupUser() {
        val t = authRepository.getToken()
        if (t != null) {
            grpc.setAccessToken(t.token)
        }
    }


    private fun getChannelConfig(): ChannelConfig {
        val uri = Uri.parse(client.address)

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
            deviceId = deviceId
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