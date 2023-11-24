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

    /**
     * For FCM, the encryption key used to encrypt push messages. Set empty if you want to unsubscribe.
     * */
    fun registerFCMToken(token: String, callback: CallbackListener<RegisterResult>)

    fun handleFCMNotification(data: Map<String, String>)


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


        /**
         * Android (Client) Application display name ; e.g.: "MyAndroidApp"
         */
        fun appName(name: String) = apply { this.name = name }


        /**
         * Android Application code version ; e.g.: "1.0"
         */
        fun appVersion(version: String) = apply { this.ver = version }


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