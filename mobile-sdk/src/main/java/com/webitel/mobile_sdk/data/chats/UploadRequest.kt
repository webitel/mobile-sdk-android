package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.FileTransferRequest
import com.webitel.mobile_sdk.domain.UploadResult


internal data class UploadRequest(
    val id: String,
    val dialog: WebitelDialog,
    val transferRequest: FileTransferRequest,
    val callback: CallbackListener<UploadResult>
)