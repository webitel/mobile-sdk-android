package com.webitel.mobile_sdk.domain

import java.util.UUID


interface Dialog {
    val id: String
    val title: String

    fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getHistory(callback: CallbackListener<List<Message>>)

    fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getUpdates(callback: CallbackListener<List<Message>>)

    fun sendMessage(message: Message.options, callback: MessageCallbackListener)

    fun downloadFile(fileId: String, observer: StreamObserver)


    /**
     * @param mid Message ID of the button.
     * @param text Button's display caption.
     * @param code Data associated with the Button.
     * @param sendId ID of the sent request. You will receive this ID in message [DialogListener.onMessageAdd()]. Optional
     * @param callback receive result from server onSent/onError.
     */
    fun sendPostback(
        mid: Long,
        text: String,
        code: String,
        sendId: String? = null,
        callback: MessageCallbackListener
    )

    fun addListener(listener: DialogListener)
    fun removeListener(listener: DialogListener)
    fun removeAllListeners()
}