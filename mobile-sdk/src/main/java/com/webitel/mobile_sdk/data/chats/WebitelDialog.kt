package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.DialogListener
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.HistoryRequest
import com.webitel.mobile_sdk.domain.MessageCallbackListener


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
        val offset = request.offset ?: lastMessage?.id ?: join
        apiDelegate.getUpdates(this, offset, request.limit, callback)
    }


    override fun getUpdates(callback: CallbackListener<List<Message>>) {
        apiDelegate.getUpdates(this, lastMessage?.id ?: join, 50, callback)
    }


    override fun sendMessage(message: Message.options, callback: MessageCallbackListener) {
        apiDelegate.sendMessage(this, message, callback)
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