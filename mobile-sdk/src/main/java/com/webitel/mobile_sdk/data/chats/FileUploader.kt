package com.webitel.mobile_sdk.data.chats

internal interface FileUploader {
    fun upload(request: UploadRequest)
    fun cancel(id: String, cleanUp: Boolean)
}