package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.File
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.ReplyMarkup


internal class WebitelMessage(
    override val sendId: String,
    override val text: String?,
    override val file: File?,
    override val from: Member,
    override val isIncoming: Boolean,
    override val id: Long,
    override val sentAt: Long,
    override val postback: Message.Postback?,
    override val replyMarkup: ReplyMarkup?,
): Message