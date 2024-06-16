package com.webitel.mobile_sdk.domain

import java.io.InputStream

class FileTransferRequest private constructor(
    val stream: InputStream,
    val fileName: String,
    val mimeType: String,
    val transferListener: TransferListener?,
    val sendId: String?) {

    class Builder(
        private val stream: InputStream,
        private val fileName: String,
        private val mimeType: String
    ) {
        private var transferListener: TransferListener? = null
        private var sendId: String? = null

        fun transferListener(listener: TransferListener) = apply { this.transferListener = listener }
        fun sendId(id: String) = apply { this.sendId = id }

        fun build(): FileTransferRequest {
            return FileTransferRequest(stream, fileName, mimeType, transferListener, sendId)
        }
    }
}