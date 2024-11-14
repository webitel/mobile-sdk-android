package com.webitel.mobile_sdk.domain

/**
 * Listener interface to handle events related to the state of a voice call.
 */
interface CallStateListener {

    /**
     * Called when a call is successfully created.
     * @param call The created call object.
     */
    fun onCreateCall(call: Call)

    /**
     * Called whenever the state of the call changes.
     * @param call The call whose state has changed.
     * @param oldState The previous state(s) of the call.
     */
    fun onCallStateChanged(call: Call, oldState: List<CallState>)

    /**
     * Called when creating a call fails.
     * @param e The error that occurred during the call creation.
     */
    fun onCreateCallFailed(e: Error)
}