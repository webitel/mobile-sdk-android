package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.CancellationToken
import com.webitel.mobile_sdk.domain.DownloadListener
import com.webitel.mobile_sdk.domain.FileTransferRequest
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.StreamObserver
import com.webitel.mobile_sdk.domain.UploadResult


internal interface ChatApiDelegate {
    fun sendMessage(
        dialog: WebitelDialog,
        options: Message.options,
        callback: MessageCallbackListener
    )


    fun getHistory(
        dialog: WebitelDialog,
        offset: Long,
        limit: Int,
        excludeKinds: List<String>,
        callback: CallbackListener<List<Message>>
    )


    fun getUpdates(
        dialog: WebitelDialog,
        offsetId: Long,
        limit: Int,
        offsetDate: Long,
        excludeKinds: List<String>,
        callback: CallbackListener<List<Message>>
    )


    fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        offset: Long,
        listener: DownloadListener
    ): CancellationToken


    fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        observer: StreamObserver
    )


    fun sendPostback(
        dialog: WebitelDialog,
        mid: Long,
        text: String,
        code: String,
        sendId: String? = null,
        callback: MessageCallbackListener
    )


    fun uploadFile(
        dialog: WebitelDialog,
        transferRequest: FileTransferRequest,
        callback: CallbackListener<UploadResult>
    ): CancellationToken
}