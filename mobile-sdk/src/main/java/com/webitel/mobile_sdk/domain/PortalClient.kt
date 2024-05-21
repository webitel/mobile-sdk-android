package com.webitel.mobile_sdk.domain

import android.app.Application
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient
import com.webitel.mobile_sdk.data.LibraryModule


interface PortalClient {

    fun getChatClient(callback: CallbackListener<ChatClient>)
    fun getVoiceClient(callback: CallbackListener<VoiceClient>)

    fun userLogin(user: User, callback: LoginListener)
    fun userLogout(callback: LoginListener)

    fun getUserSession(callback: CallbackListener<Session>)

    fun setAccessToken(token: String, callback: CallbackListener<Session>)

    /**
     * Register device PUSH subscription
     * */
    fun registerDevice(pushToken: String, callback: CallbackListener<RegisterResult>)

    fun handleFCMNotification(data: Map<String, String>)


    fun getConnectState(): ConnectState

    fun addConnectListener(listener: ConnectListener)

    fun removeConnectListener(listener: ConnectListener)

    fun openConnect()

    fun closeConnect()


    /**
     * @param application Application context.
     * @param address Webitel Customer Portal service host address ; e.g.: grpcs://dev.webitel.com:443
     * @param token Webitel (Client: Portal App) token issued.
     */
    data class Builder(
        internal val application: Application,
        internal var address: String,
        internal var token: String
    ) {

        internal var ver: String = "0.0.0"
        internal var name: String = ""
        internal var fcmToken: String = ""
        internal var deviceId: String = ""


        /**
         * Android (Client) Application display name ; e.g.: "MyAndroidApp"
         */
        fun appName(name: String) = apply { this.name = name }


        /**
         * Android Application code version ; e.g.: "1.0"
         */
        fun appVersion(version: String) = apply { this.ver = version }


        fun deviceId(value: String) = apply { this.deviceId = value }


//        /**
//         * FCM Token. From Firebase
//         */
//        fun fcmToken(fcmToken: String) = apply { this.fcmToken = fcmToken }


        fun build(): PortalClient {
            LibraryModule.initializeDI(application)
            return WebitelPortalClient(this)
        }
    }
}