package com.webitel.mobile_sdk.domain


interface StreamObserver {
    fun onNext(value: ByteArray)
    fun onError(e: Error)
    fun onCompleted()
}