package com.webitel.mobile_sdk.domain


interface MessageCallbackListener {
    fun onSent(m: Message)
    fun onError(e: Error)
}