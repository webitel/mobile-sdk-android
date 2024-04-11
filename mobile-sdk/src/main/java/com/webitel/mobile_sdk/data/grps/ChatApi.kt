package com.webitel.mobile_sdk.data.grps

import io.grpc.stub.StreamObserver
import webitel.chat.MessageOuterClass
import webitel.portal.Media


internal interface ChatApi: BaseApi {
    fun startPing()
    fun stopPing()

    fun openConnection()

    fun uploadFile(
        streamObserver: StreamObserver<MessageOuterClass.File>
    ): StreamObserver<Media.UploadMedia>

    fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: StreamObserver<Media.MediaFile>
    )
}