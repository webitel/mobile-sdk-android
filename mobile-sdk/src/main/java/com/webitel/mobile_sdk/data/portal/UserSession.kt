package com.webitel.mobile_sdk.data.portal

import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.User
import com.webitel.mobile_sdk.domain.Session


internal class UserSession(
    override val user: User,
    override val isChatAvailable: Boolean,
    override val isVoiceAvailable: Boolean,
    override val isPushEnabled: Boolean,
    val chatAccount: Member?
) : Session