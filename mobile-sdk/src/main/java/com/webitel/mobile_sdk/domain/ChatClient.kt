package com.webitel.mobile_sdk.domain


interface ChatClient {
    fun getServiceDialog(callback: CallbackListener<Dialog>)
    fun getDialogs(callback: CallbackListener<List<Dialog>>)
}