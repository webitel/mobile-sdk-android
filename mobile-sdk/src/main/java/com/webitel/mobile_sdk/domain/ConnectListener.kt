package com.webitel.mobile_sdk.domain


/**
 * An interface to listen for changes in connection state.
 *
 * This interface provides a method for monitoring connection state transitions
 * within the system. It notifies listeners whenever there is a change in
 * connection status.
 */
interface ConnectListener {

    /**
     * Called when the connection state changes.
     *
     * This method is triggered whenever there is a transition from one
     * `ConnectState` to another. Implement this method to handle specific
     * actions or updates required on connection state changes.
     *
     * @param from The previous state of the connection.
     * @param to The new state of the connection after the transition.
     */
    fun onStateChanged(from: ConnectState, to: ConnectState)
}