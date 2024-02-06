package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.Error


internal class WebitelMessage(
    val reqId: String?,
    override val text: String?,
    override val file: WebitelFile?,
    override val from: Member,
    override val isIncoming: Boolean,
    private var _id: Long = 0,
    private var _sentAt: Long = 0
): Message {

    override val error: Error?
        get() = _e
    override val id: Long
        get() = _id
    override val sentAt: Long
        get() = _sentAt
    override val editAt: Long
        get() = _editAt

    private var _e: Error? = null
    private var _editAt: Long = 0


    fun setId(i: Long) {
        _id = i
    }
    fun setError(e: Error) {
        _e = e
    }
    fun setSendAt(at: Long) {
        _sentAt = at
    }
    fun setEditAt(at: Long) {
        _editAt = at
    }
}