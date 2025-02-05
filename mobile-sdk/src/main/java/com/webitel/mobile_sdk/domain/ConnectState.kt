package com.webitel.mobile_sdk.domain


sealed class ConnectState {
    /**
     * The initial state, indicating no connection attempt has been made yet.
     */
    object None : ConnectState() {
        override fun toString() = "None"
    }

    /**
     * The state when a connection attempt is currently in progress.
     */
    object Connecting : ConnectState() {
        override fun toString() = "Connecting"
    }

    /**
     * The state indicating that the connection is successfully established and ready for use.
     */
    object Ready : ConnectState() {
        override fun toString() = "Ready"
    }

    /**
     * The state indicating that the connection has been disconnected, with an associated error describing the reason for disconnection.
     */
    data class Disconnected(val reason: Error) : ConnectState() {
        override fun toString() = "Disconnected(reason=$reason)"
    }
}