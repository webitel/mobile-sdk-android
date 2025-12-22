package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import webitel.chat.MessageOuterClass
import webitel.portal.Media


internal interface ChatApi: BaseApi {
    fun openConnection()

    fun closeConnection()

    fun getConnectState(): ConnectState

    fun addConnectListener(listener: ConnectListener)

    fun removeConnectListener(listener: ConnectListener)

    fun addListener(listener: GrpcListener)

    fun removeListener(listener: GrpcListener)

    fun removeAllListeners()

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