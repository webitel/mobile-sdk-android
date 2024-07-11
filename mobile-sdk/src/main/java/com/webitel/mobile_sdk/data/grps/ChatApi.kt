package com.webitel.mobile_sdk.data.grps

import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import webitel.chat.MessageOuterClass
import webitel.portal.Media


internal interface ChatApi: BaseApi {
    fun startPing()
    fun stopPing()

    fun openConnection()

    fun closeConnection()

    fun uploadFile(
        streamObserver: StreamObserver<MessageOuterClass.File>
    ): StreamObserver<Media.UploadMedia>


    fun uploadFile(
        responseObserver: ClientResponseObserver<Media.UploadRequest, Media.UploadProgress>,
    ): StreamObserver<Media.UploadRequest>


    fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: ClientResponseObserver<Media.GetFileRequest, Media.MediaFile>
    )


    fun downloadFile(
        request: Media.GetFileRequest,
        streamObserver: StreamObserver<Media.MediaFile>
    )
}