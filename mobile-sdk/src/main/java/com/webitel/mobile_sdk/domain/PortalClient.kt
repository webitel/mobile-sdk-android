package com.webitel.mobile_sdk.domain

import android.app.Application
import android.util.Log
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient
import com.webitel.mobile_sdk.data.LibraryModule


interface PortalClient {

    fun getChatClient(callback: CallbackListener<ChatClient>)
    fun getVoiceClient(callback: CallbackListener<VoiceClient>)

    fun userLogin(user: User, callback: LoginListener)
    fun userLogout(callback: LoginListener)

    fun getUserSession(callback: CallbackListener<Session>)

    /**
     * Sets and immediately validates the token. The result will be returned in the callback
     * */
    fun setAccessToken(token: String, callback: CallbackListener<Session>)

    /**
     * Sets the token in headers without validation
     */
    fun  setAccessTokenHeader(token: String)

    /**
     * Sets the access token.
     *
     * This function stores the provided access token and calls the callback upon completion of the operation.
     *
     * @param token The access token to be set.
     * @param callback called when the operation is finished, where:
     *
     * `.onSuccess`: If the token was successfully set.
     * `.onError`: If an error occurred while setting the token, along with an `Error` object describing the error.
     */
    fun  setAccessTokenHeader(token: String, callback: CallbackListener<Unit>)

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
        internal var deviceId: String = ""
        internal var logLevel: LogLevel = LogLevel.ERROR

        internal var keepAliveTime: Long = 10
        internal var keepAliveTimeout: Long = 10


        /**
         * Android (Client) Application display name ; e.g.: "MyAndroidApp"
         */
        fun appName(name: String) = apply { this.name = name }


        /**
         * Android Application code version ; e.g.: "1.0"
         */
        fun appVersion(version: String) = apply { this.ver = version }


        fun deviceId(value: String) = apply { this.deviceId = value }


        /**
         * The logLevel method sets the log level for error and message reporting.
         * Specifies the log level to set. The following are the valid options described in ascending order:
         *  - debug — Specifies a log level in which all messages are logged.
         *  - info — Specifies a log level in which informational, warning, and error messages are logged.
         *  - warn — Specifies a log level in which warning and error messages are logged.
         *  - error — Specifies a log level in which only error messages are logged.
         *  - off — disables all logs.
         *  Default is LogLevel.error
         */
        fun logLevel(value: LogLevel) = apply { this.logLevel = value }


        /**
         * Sets the time without read activity before sending a keepalive ping.
         * If the value is too small, it may be increased to a reasonable minimum.
         * A value of Long.MAX_VALUE disables keepalive. Defaults to 10 seconds.
         *
         * @param seconds The time in seconds without read activity before sending a keepalive ping.
         */
        fun setKeepAliveTime(seconds: Long) = apply { this.keepAliveTime = seconds }


        /**
         * Sets the time waiting for read activity after sending a keepalive ping.
         * If the time expires without any read activity, the connection is considered dead.
         * If the value is too small, it may be increased to a reasonable minimum. Defaults to 10 seconds.
         *
         * @param seconds The time in seconds to wait for a read activity after sending a keepalive ping.
         */
        fun setKeepAliveTimeout(seconds: Long) = apply { this.keepAliveTimeout = seconds }


        fun build(): PortalClient {
            LibraryModule.initializeDI(application)
            return WebitelPortalClient(this)
        }
    }
}