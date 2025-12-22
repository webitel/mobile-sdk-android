package com.webitel.mobile_sdk.domain

import android.app.Application
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient
import com.webitel.mobile_sdk.data.LibraryModule


/**
 * Interface for interacting with the Portal Client, allowing actions such as logging in, managing sessions,
 * handling user notifications, and managing the state of chat and voice clients.
 */
interface PortalClient {

    /**
     * Provides access to the ChatClient instance without performing an authorization check.
     * Use this property if you are handling authorization separately.
     */
    val chatClient: ChatClient


    /**
     * Asynchronously returns the ChatClient instance after checking user authorization.
     * If the user is authorized, the ChatClient is returned. Otherwise, a UNAUTHENTICATED error is returned.
     *
     * @param callback The callback that receives the ChatClient instance or an authorization error.
     */
    fun getChatClient(callback: CallbackListener<ChatClient>)


    /**
     * Logs in the user with the provided user data.
     * @param user The user to log in.
     * @param callback The callback to handle the login result.
     */
    fun userLogin(user: User, callback: LoginListener)


    /**
     * Logs out the current user.
     * @param callback The callback to handle the logout result.
     */
    fun userLogout(callback: LoginListener)


    /**
     * Retrieves the current user session.
     * @param callback The callback to handle the response with the current session.
     */
    fun getUserSession(callback: CallbackListener<Session>)


    /**
     * Sets the access token and immediately validates it.
     * If the token is valid, the callback will return the user session associated with the token.
     * In case of an error, the callback will return an error.
     *
     * @param token The access token to set.
     * @param callback The callback to handle the response with the user session or an error.
     */
    fun setAccessToken(token: String, callback: CallbackListener<Session>)


    /**
     * Sets the access token in the headers.
     * If there is an open connection, the token will be validated and updated in the connection.
     * If the connection is closed, the token is set in the headers and will be used when the connection is opened.
     *
     * @param token The access token to set in the headers.
     */
    fun  setAccessTokenHeader(token: String)


    /**
     * Sets the access token in the headers.
     * If there is an open connection, the token will be validated and updated in the connection.
     * If the connection is closed, the token is set in the headers and will be used when the connection is opened.
     *
     * @param token The access token to set in the headers.
     * @param callback The optional callback to handle the result after setting the token in the headers.
     */
    fun  setAccessTokenHeader(token: String, callback: CallbackListener<Unit>)


    /**
     * Registers the device for push notifications with the provided token.
     * @param pushToken The push token for the device.
     * @param callback The callback to handle the registration result.
     */
    fun registerDevice(pushToken: String, callback: CallbackListener<RegisterResult>)


    /**
     * Handles incoming FCM (Firebase Cloud Messaging) notifications.
     * @param data The notification data to process.
     */
    fun handleFCMNotification(data: Map<String, String>)


    /**
     * Retrieves the current connection state.
     * @return The current connection state.
     */
    fun getConnectState(): ConnectState


    /**
     * Adds a listener for connection state changes.
     * @param listener The listener to be added.
     */
    fun addConnectListener(listener: ConnectListener)


    /**
     * Removes a previously added connection state listener.
     * @param listener The listener to be removed.
     */
    fun removeConnectListener(listener: ConnectListener)


    /**
     * Opens a connection to the portal.
     */
    fun openConnect()


    /**
     * Closes the connection to the portal.
     */
    fun closeConnect()


    /**
     * Builder for configuring and creating an instance of PortalClient.
     * Allows setting parameters like application context, server address, access token, logging level, and connection settings.
     */
    data class Builder(
        val application: Application,
        var address: String,
        var token: String
    ) {

        internal var ver: String = "0.0.0"
        internal var name: String = ""
        internal var deviceId: String = ""
        internal var logLevel: LogLevel = LogLevel.ERROR

        internal var keepAliveTime: Long = 10
        internal var keepAliveTimeout: Long = 10
        internal var transport: Transport = Transport.GRPC


        /**
         * Sets the Android application display name.
         * @param name The display name of the application.
         * @return The Builder instance for method chaining.
         */
        fun appName(name: String) = apply { this.name = name }


        /**
         * Sets the Android application version.
         * @param version The version of the application.
         * @return The Builder instance for method chaining.
         */
        fun appVersion(version: String) = apply { this.ver = version }


        /**
         * Sets the device ID for the client.
         * @param value The device ID.
         * @return The Builder instance for method chaining.
         */
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
        fun keepAliveTime(seconds: Long) = apply { this.keepAliveTime = seconds }


        /**
         * Sets the time waiting for read activity after sending a keepalive ping.
         * If the time expires without any read activity, the connection is considered dead.
         * If the value is too small, it may be increased to a reasonable minimum. Defaults to 10 seconds.
         *
         * @param seconds The time in seconds to wait for a read activity after sending a keepalive ping.
         */
        fun keepAliveTimeout(seconds: Long) = apply { this.keepAliveTimeout = seconds }


        /**
         * Sets the client transport.
         *
         * Defaults to gRPC if not specified.
         *
         * @param transport the transport to use
         */
        fun transport(transport: Transport) = apply {
            this.transport = transport
        }


        /**
         * Switches transport to WebSocket.
         *
         * By default, the client uses gRPC transport.
         * When enabled, all incoming and outgoing notifications
         * are handled via a WebSocket connection.
         */
        fun useWebSocket() = apply {
            this.transport = Transport.WEBSOCKET
        }


        /**
         * Builds and returns a PortalClient instance.
         * @return A new instance of PortalClient.
         */
        fun build(): PortalClient {
            LibraryModule.initializeDI(application)
            return WebitelPortalClient(this)
        }
    }
}