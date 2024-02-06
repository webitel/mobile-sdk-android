package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.Message

internal class WebitelFile(
    override val fileName: String,
    override val type: String,
    override val size: Long,
    fileId: String
) : Message.File {
    override lateinit var id: String
        private set
    init {
        id = fileId
    }

    fun setFileId(id: String) {
        this.id = id
    }
}