package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.CallbackListener
import webitel.portal.Connect


internal interface BaseApi {
    fun sendMessage(request: Connect.Request)
    fun ping(callback: CallbackListener<Unit>)
    fun isStateReady(requestConnection: Boolean = true): Boolean
    fun isStreamOpened(): Boolean
}