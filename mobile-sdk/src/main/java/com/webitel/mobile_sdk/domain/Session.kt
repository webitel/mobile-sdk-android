package com.webitel.mobile_sdk.domain


interface Session {
    val user: User

    val isChatAvailable: Boolean
    val isVoiceAvailable: Boolean

    val isPushEnabled: Boolean
}
