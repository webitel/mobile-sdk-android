package com.webitel.mobile_sdk.data.calls.sip

import java.lang.Exception

internal interface SipCallbacks {
    fun onSipRegistrationSuccess()
    fun onSipRegistrationFailure(errorMessage: String?)
    fun onIncomingCallPJSIP(callId: Int, webitelId: String)
}

internal interface SipCallCallbacks {
    fun onAnswerCallPJSIP(callId: Int, webitelId: String)
    fun onDisconnectPJSIP(callId: Int, webitelId: String)
    fun onActiveCallPJSIP(callId: Int, webitelId: String)
    fun onHoldCallPJSIP(callId: Int, webitelId: String)
    fun onCallErrorPJSIP(e: Exception?)
}