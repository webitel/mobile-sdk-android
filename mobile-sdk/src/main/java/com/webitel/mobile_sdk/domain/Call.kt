package com.webitel.mobile_sdk.domain

interface Call {
    val id: String
    val toName: String
    val toNumber: String
    val from: String
    val state: List<CallState>
    val dtmfHistory: String
    val answeredAt: Long
    fun toggleHold()
    fun toggleMute()
    fun toggleLoudspeaker()
    fun sendDigits(value: String)
    fun addListener(listener: CallStateListener)
    fun removeListener(listener: CallStateListener)
    fun removeAllListeners()
    fun disconnect()
}
