package com.webitel.mobile_sdk.domain


interface VoiceClient {
    val activeCall: Call?
    fun makeCall(listener: CallStateListener)
}