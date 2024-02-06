package com.webitel.mobile_sdk.domain


interface MediaUploadListener {
    fun onProgress(bytesSent: Long)
    fun onCompleted()
}