package com.webitel.mobile_sdk.data.grps


internal interface ChatApi: BaseApi {
    fun startPing()
    fun stopPing()
}