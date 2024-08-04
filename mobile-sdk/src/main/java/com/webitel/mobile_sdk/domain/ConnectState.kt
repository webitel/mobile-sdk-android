package com.webitel.mobile_sdk.domain


sealed class ConnectState {
    /**
     * The initial state, indicating no connection attempt has been made yet.
     */
    object None : ConnectState()

    /**
     * The state when a connection attempt is currently in progress.
     */
    object Connecting : ConnectState()

    /**
     * The state indicating that the connection is successfully established and ready for use.
     */
    object Ready : ConnectState()

    /**
     * The state indicating that the connection has been disconnected, with an associated error describing the reason for disconnection.
     */
    data class Disconnected(val reason: Error) : ConnectState()
}