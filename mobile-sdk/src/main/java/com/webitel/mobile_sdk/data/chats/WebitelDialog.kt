package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.DialogListener
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.HistoryRequest
import com.webitel.mobile_sdk.domain.InvalidProcessIdException
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.TransferControl
import com.webitel.mobile_sdk.domain.TransferListener


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
        apiDelegate.getHistory(this, request.offset ?: 0, request.limit, callback)
    }


    override fun getHistory(callback: CallbackListener<List<Message>>) {
        apiDelegate.getHistory(this, 0, 50, callback)
    }


    override fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>) {
        val offset = request.offset ?: lastMessage?.id ?: 0
        apiDelegate.getUpdates(this, offset, request.limit, join, callback)
    }


    override fun getUpdates(callback: CallbackListener<List<Message>>) {
        apiDelegate.getUpdates(this, lastMessage?.id ?: 0, 50, join, callback)
    }


    override fun sendMessage(message: Message.options, callback: MessageCallbackListener) {
        apiDelegate.sendMessage(this, message, callback)
    }


    override fun downloadFile(fileId: String, listener: TransferListener): TransferControl {
        return apiDelegate.downloadFile(this, fileId, 0, listener)
    }


    override fun downloadFile(
        fileId: String,
        offset: Long,
        listener: TransferListener
    ): TransferControl {
        return apiDelegate.downloadFile(this, fileId, offset, listener)
    }


    override fun downloadFile(listener: TransferListener, pid: String): TransferControl {
        val process = pid.split('/')
        val fileId = process.getOrNull(1)
        val offset = process.getOrNull(2)?.toLongOrNull()

        if (fileId.isNullOrEmpty() || offset == null) {
            throw InvalidProcessIdException(
                message = "pid is invalid.",
            )
        }

        return apiDelegate.downloadFile(this, fileId, offset,  listener)
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