package com.webitel.mobile_sdk.domain


interface ChatListener {
    fun onMessageAdded(message: Message)
    fun onMessageUpdated(message: Message)
    fun onMessageDeleted(message: Message)
    fun onMemberAdded(member: Member)
    fun onMemberDeleted(member: Member)
}