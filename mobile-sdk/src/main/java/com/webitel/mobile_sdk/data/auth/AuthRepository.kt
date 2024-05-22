package com.webitel.mobile_sdk.data.auth

import android.util.Log
import com.google.protobuf.Any
import com.webitel.mobile_sdk.data.auth.storage.AuthStorage
import com.webitel.mobile_sdk.data.grps.AuthApi
import com.webitel.mobile_sdk.data.grps.GrpcListener
import com.webitel.mobile_sdk.data.grps.`is`
import com.webitel.mobile_sdk.data.grps.pack
import com.webitel.mobile_sdk.data.grps.unpack
import com.webitel.mobile_sdk.data.portal.UserSession
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.User
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.LoginListener
import com.webitel.mobile_sdk.domain.RegisterResult
import webitel.portal.Account.Identity
import webitel.portal.Auth
import webitel.portal.Connect
import webitel.portal.CustomerGrpc
import webitel.portal.CustomerOuterClass.InspectRequest
import webitel.portal.CustomerOuterClass.RegisterDeviceRequest
import webitel.portal.CustomerOuterClass.RegisterDeviceResponse
import webitel.portal.Push.DevicePush
import java.util.UUID


internal class AuthRepository(
    private val storage: AuthStorage,
    private val authApi: AuthApi
) : GrpcListener {

    private val requests: HashMap<String, CallbackListener<*>> = hashMapOf()


    fun getToken(): AccessToken? {
        return storage.getAccessToken()
    }


    @Synchronized
    fun userLogin(appToken: String, user: User, callback: CallbackListener<UserSession>) {
        try {
            val identity = Identity
                .newBuilder()
                .setIss(user.iss)
                .setSub(user.sub)
                .setName(user.name)
                .setEmail(user.email)
                .setEmailVerified(user.emailVerified)
                .setPhoneNumber(user.phoneNumber)
                .setPhoneNumberVerified(user.phoneNumberVerified)
                .setLocale(user.locale)
                .build()

            authApi.login(appToken, identity, object : CallbackListener<LoginResponse> {
                override fun onSuccess(t: LoginResponse) {
                    storage.saveAccessToken(t.token)
                    callback.onSuccess(t.session)
                }

                override fun onError(e: Error) {
                    callback.onError(e)
                }
            })

        } catch (e: Exception) {
            callback.onError(
                Error(
                    e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    @Synchronized
    fun logout(callback: LoginListener) {
        authApi.logout(object : CallbackListener<Unit> {
            override fun onSuccess(t: Unit) {
                destroy()
                callback.onLogoutFinished()
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    @Synchronized
    fun getSession(callback: CallbackListener<UserSession>) {
        if (authApi.isStreamOpened()) {
            val reqId = UUID.randomUUID().toString()
            val i = InspectRequest
                .newBuilder()
                .build()
            val request = Connect.Request.newBuilder()
                .setId(reqId)
                .setPath(CustomerGrpc.getInspectMethod().bareMethodName)
                .setData(Any.newBuilder().pack(i))
                .build()
            requests[reqId] = callback
            authApi.sendMessage(request)
        } else {
            authApi.inspect(callback)
        }
    }


    fun setAccessToken(
        token: String,
        callback: CallbackListener<UserSession>) {
        authApi.setSession(token, callback)
    }


    fun registerFcm(
        token: String,
        callback: CallbackListener<RegisterResult>) {
        if (authApi.isStreamOpened()) {
            val d = DevicePush.newBuilder().setFCM(token).build()
            val reqId = UUID.randomUUID().toString()
            val i = RegisterDeviceRequest
                .newBuilder()
                .setPush(d)
                .build()
            val request = Connect.Request.newBuilder()
                .setId(reqId)
                .setPath(CustomerGrpc.getRegisterDeviceMethod().bareMethodName)
                .setData(Any.newBuilder().pack(i))
                .build()
            requests[reqId] = callback
            authApi.sendMessage(request)
        } else {
            authApi.registerFcm(token, callback)
        }
    }


    private fun destroy() {
        storage.clear()
    }


    override fun onResponse(response: Connect.Response) {
        val request = requests.remove(response.id)
            ?: return

        if (!response.err.message.isNullOrEmpty()) {
            Log.e(
                "onResponse",
                "${response.err.message}; code - ${response.err.code}"
            )
            request.onError(
                Error(
                    message = response.err.message,
                    code = Code.forNumber(response.err.code)
                )
            )

        } else {
            if (response.data.`is`(Auth.AccessToken::class.java)) {

                val s = response.data.unpack(Auth.AccessToken::class.java)
                if (s == null) {
                    request.onError(
                        Error(
                            message = "Could not UNPACK the response. ${response.data.typeUrl}",
                            code = Code.DATA_LOSS
                        )
                    )
                    return
                }

                val chatAccount = if (s.chat != null && s.chat.user != null) {
                    Member(
                        id = s.chat.user.id,
                        name = s.chat.user.name,
                        type = s.chat.user.type
                    )
                } else null

                try {
                    val user = User.Builder(
                        iss = s.user.identity.iss,
                        sub = s.user.identity.sub,
                        name = s.user.identity.name
                    )
                        .email(s.user.identity.email)
                        .emailVerified(s.user.identity.emailVerified)
                        .phoneNumber(s.user.identity.phoneNumber)
                        .phoneNumberVerified(s.user.identity.phoneNumberVerified)
                        .locale(s.user.identity.locale)
                        .build()

                    request as CallbackListener<UserSession>
                    request.onSuccess(
                        UserSession(
                            user = user,
                            isChatAvailable = s.scopeList.contains("chat"),
                            isVoiceAvailable = s.scopeList.contains("call"),
                            chatAccount
                        )
                    )
                }catch (_: Exception){}
            } else if (response.data.`is`(RegisterDeviceResponse::class.java)) {
                try {
                    request as CallbackListener<RegisterResult>
                    request.onSuccess(
                        RegisterResult()
                    )
                }catch (_: Exception){}
            } else {
                request.onError(
                    Error(
                        message = "Could not UNPACK response. ${response}",
                        code = Code.DATA_LOSS
                    )
                )
            }
        }
    }


    override fun onConnectionError(e: Error) {
        requests.forEach {
            it.value.onError(e)
            requests.remove(it.key)
        }
    }


    override fun onConnectionReady() {}
}