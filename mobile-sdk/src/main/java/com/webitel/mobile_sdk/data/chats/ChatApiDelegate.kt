package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.StreamObserver


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
        callback: CallbackListener<List<Message>>
    )


    fun getUpdates(
        dialog: WebitelDialog,
        offsetId: Long,
        limit: Int,
        offsetDate: Long,
        callback: CallbackListener<List<Message>>
    )


    fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        observer: StreamObserver
    )
}