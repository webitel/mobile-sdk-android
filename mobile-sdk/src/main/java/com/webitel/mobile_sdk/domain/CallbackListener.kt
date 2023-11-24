package com.webitel.mobile_sdk.domain


interface CallbackListener<T> {
    fun onSuccess(t: T)
    fun onError(e: Error)
}