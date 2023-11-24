package com.webitel.mobile_sdk.domain


interface MessageCallbackListener {
    fun onSend(m: Message)
    fun onSent(m: Message)
    fun onError(e: Error)
}