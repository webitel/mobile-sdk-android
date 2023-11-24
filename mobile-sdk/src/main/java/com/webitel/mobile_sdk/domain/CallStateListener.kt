package com.webitel.mobile_sdk.domain


interface CallStateListener {
    fun onCreateCall(call: Call)

    fun onCallStateChanged(call: Call, oldState: List<CallState>)

    fun onCreateCallFailed(e: Error)
}