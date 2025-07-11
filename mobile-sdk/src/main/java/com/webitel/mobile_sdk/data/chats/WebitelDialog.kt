package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.DialogListener
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.CancellationToken
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.DownloadListener
import com.webitel.mobile_sdk.domain.FileTransferRequest
import com.webitel.mobile_sdk.domain.HistoryRequest
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.StreamObserver
import com.webitel.mobile_sdk.domain.UploadResult


internal class WebitelDialog(
    private val apiDelegate: ChatApiDelegate,
    override val id: String,
    override val title: String,
    val type: String,
    var inbox: Int,
    var leftAt: Long,
    var join: Long,
    var lastMessage: Message? = null
) : Dialog {

    private val listeners: DialogListeners = DialogListeners()

    override fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>) {
        apiDelegate.getHistory(this, request.offset ?: 0, request.limit, request.excludeKinds, callback)
    }


    override fun getHistory(callback: CallbackListener<List<Message>>) {
        apiDelegate.getHistory(this, 0, 50, emptyList(), callback)
    }


    override fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>) {
        val offset = request.offset ?: lastMessage?.id ?: 0
        apiDelegate.getUpdates(this, offset, request.limit, join, request.excludeKinds, callback)
    }


    override fun getUpdates(callback: CallbackListener<List<Message>>) {
        apiDelegate.getUpdates(this, lastMessage?.id ?: 0, 50, join, emptyList(), callback)
    }


    override fun sendMessage(message: Message.options, callback: MessageCallbackListener) {
        apiDelegate.sendMessage(this, message, callback)
    }


    @Deprecated("Use 'downloadFile(fileId: String, listener: DownloadListener): CancellationToken' instead")
    override fun downloadFile(fileId: String, observer: StreamObserver) {
        apiDelegate.downloadFile(this, fileId, observer)
    }


    override fun downloadFile(fileId: String, listener: DownloadListener): CancellationToken {
        return apiDelegate.downloadFile(this, fileId, 0, listener)
    }


    override fun downloadFile(
        fileId: String,
        offset: Long,
        listener: DownloadListener
    ): CancellationToken {
        return apiDelegate.downloadFile(this, fileId, offset, listener)
    }


    override fun uploadFile(
        request: FileTransferRequest,
        callback: CallbackListener<UploadResult>
    ): CancellationToken {
        return apiDelegate.uploadFile(this, request, callback)
    }


    override fun sendPostback(
        mid: Long,
        text: String,
        code: String,
        sendId: String?,
        callback: MessageCallbackListener
    ) {
        apiDelegate.sendPostback(this, mid, text, code, sendId, callback)
    }


    override fun addListener(listener: DialogListener) {
        listeners.addListener(listener)
    }


    override fun removeListener(listener: DialogListener) {
        listeners.removeListener(listener)
    }


    override fun removeAllListeners() {
        listeners.removeAllListeners()
    }


    fun onReceiveNewMessage(message: WebitelMessage) {
        lastMessage = message
        listeners.onMessageAdded(message)
    }
}