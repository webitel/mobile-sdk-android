package com.webitel.mobile_sdk.domain


/**
 * Interface representing a client responsible for handling voice calls.
 * It can make a call and maintain a reference to the active call, if any.
 */
interface VoiceClient {

    /** The currently active call, or null if no call is active */
    val activeCall: Call?


    /**
     * Initiates a new call and provides a listener to handle the call's state changes.
     * @param listener The listener to handle call events.
     */
    fun makeCall(listener: CallStateListener)
}