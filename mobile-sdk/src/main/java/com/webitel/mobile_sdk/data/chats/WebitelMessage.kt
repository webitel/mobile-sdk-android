package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Keyboard
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message


internal class WebitelMessage(
    override val sendId: String,
    override val text: String?,
    override val file: WebitelFile?,
    override val from: Member,
    override val isIncoming: Boolean,
    override val id: Long,
    override val sentAt: Long,
    override val postback: Message.Postback?,
    override val keyboard: Keyboard?,
): Message