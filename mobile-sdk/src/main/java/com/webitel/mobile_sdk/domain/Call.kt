package com.webitel.mobile_sdk.domain


/**
 * Interface representing a single voice call.
 * Provides methods to interact with the call and manage its state.
 */
interface Call {
    /** The unique identifier for the call */
    val id: String

    /** The name of the person being called */
    val toName: String

    /** The phone number of the person being called */
    val toNumber: String

    /** The phone number or identifier of the caller */
    val from: String

    /** The current state(s) of the call (e.g., active, hold, mute) */
    val state: List<CallState>

    /** History of DTMF digits sent during the call */
    val dtmfHistory: String

    /** The timestamp (in milliseconds) when the call was answered */
    val answeredAt: Long

    /** Toggles the hold state of the call */
    fun toggleHold()

    /** Toggles the mute state of the call */
    fun toggleMute()

    /** Toggles the loudspeaker state of the call */
    fun toggleLoudspeaker()

    /** Sends DTMF digits to the call */
    fun sendDigits(value: String)

    /**
     * Adds a listener to observe call state changes.
     * @param listener The listener to add.
     */
    fun addListener(listener: CallStateListener)

    /**
     * Removes a previously added listener.
     * @param listener The listener to remove.
     */
    fun removeListener(listener: CallStateListener)

    /** Removes all listeners associated with the call */
    fun removeAllListeners()

    /** Disconnects (ends) the call */
    fun disconnect()
}
