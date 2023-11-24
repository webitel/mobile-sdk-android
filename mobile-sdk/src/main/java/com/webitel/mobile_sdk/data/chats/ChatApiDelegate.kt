package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.MessageCallbackListener


internal interface ChatApiDelegate {
    fun sendMessage(
        dialog: WebitelDialog,
        message: Message.options,
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
        offset: Long,
        limit: Int,
        callback: CallbackListener<List<Message>>
    )
}