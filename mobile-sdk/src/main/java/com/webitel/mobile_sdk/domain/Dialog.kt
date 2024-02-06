package com.webitel.mobile_sdk.domain


interface Dialog {
    val id: String
    val title: String

    fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getHistory(callback: CallbackListener<List<Message>>)

    fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getUpdates(callback: CallbackListener<List<Message>>)

    fun sendMessage(message: Message.options, callback: MessageCallbackListener)

    fun downloadFile(fileId: String, observer: StreamObserver)

    fun addListener(listener: DialogListener)
    fun removeListener(listener: DialogListener)
    fun removeAllListeners()
}