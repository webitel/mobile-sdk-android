package com.webitel.mobile_sdk.domain


interface Dialog {
    val id: String
    val title: String

    fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getHistory(callback: CallbackListener<List<Message>>)

    fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getUpdates(callback: CallbackListener<List<Message>>)

    fun sendMessage(message: Message.options, callback: MessageCallbackListener)

    fun addListener(listener: ChatListener)
    fun removeListener(listener: ChatListener)
    fun removeAllListeners()
}