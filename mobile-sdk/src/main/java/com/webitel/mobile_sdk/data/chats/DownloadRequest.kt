package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.DownloadListener


internal data class DownloadRequest(
    val id: String,
    val dialog: WebitelDialog,
    val fileId: String,
    val offset: Long,
    val listener: DownloadListener
)