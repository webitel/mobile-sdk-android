package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.Error
import webitel.portal.Connect
import webitel.portal.Messages.UpdateNewMessage

internal interface GrpcListener {
    fun onResponse(response: Connect.Response)
    fun onNewMessage(message: UpdateNewMessage)
    fun onConnectionError(e: Error)
    fun onConnectionReady()
}