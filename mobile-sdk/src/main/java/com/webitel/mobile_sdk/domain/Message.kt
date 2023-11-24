package com.webitel.mobile_sdk.domain

import java.io.InputStream

interface Message {
    val id: Long
    val text: String?
    val file: File?

    val from: Member

    val isIncoming: Boolean

    val sentAt: Long
    val editAt: Long

    val error: Error?


    class options {
        internal var text: String? = null
            private set

        internal var stream: InputStream? = null
            private set

        fun withText(text: String) = apply { this.text = text }

        //fun withMedia(stream: InputStream, mimeType: String) = apply { this.stream = stream }
    }

    abstract class File (val fileName: String, val type: String, val size: Long) {
        abstract fun getContentTemporaryUrl(callback: (String) -> Void)
    }
}

