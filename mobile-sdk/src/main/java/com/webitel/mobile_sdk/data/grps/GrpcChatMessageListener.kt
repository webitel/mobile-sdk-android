package com.webitel.mobile_sdk.data.grps

import webitel.portal.Messages.UpdateNewMessage


internal interface GrpcChatMessageListener: GrpcListener {
    fun onNewMessage(message: UpdateNewMessage)
}