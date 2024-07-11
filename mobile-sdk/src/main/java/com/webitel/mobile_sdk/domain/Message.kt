package com.webitel.mobile_sdk.domain

import java.io.InputStream

interface Message {
    /**
     * Unique message identifier inside this chat.
     */
    val id: Long

    /**
     * Client generated request id.
     */
    val sendId: String

    /**
     * Message Text.
     */
    val text: String?

    /**
     * Message Media. Attachment.
     */
    val file: File?

    /**
     * Sender of the message.
     */
    val from: Member

    /**
     * Message is incoming
     */
    val isIncoming: Boolean

    /**
     * Timestamp when this message was sent (published).
     */
    val sentAt: Long


    /**
     * Postback. Reply Button Clicked.
     * */
    val postback: Postback?

    /**
     * Reply markup (buttons)
     * */
    val replyMarkup: ReplyMarkup?


    class options {
        internal var text: String? = null
            private set

        internal var sendId: String? = null
            private set

        internal var fileName: String? = null
            private set

        internal var mimeType: String? = null
            private set

        internal var stream: InputStream? = null
            private set

        internal var listener: MediaUploadListener? = null
            private set

        fun sendId(id: String) = apply { this.sendId = id }

        fun withText(text: String) = apply { this.text = text }

        @Deprecated(message = "Use 'dialog.sendFile(FileTransferRequest, MessageCallbackListener): CancellationToken' instead")
        fun withMedia(stream: InputStream, fileName: String, mimeType: String) = apply {
            this.stream = stream
            this.mimeType = mimeType
            this.fileName = fileName
        }

        fun uploadListener(listener: MediaUploadListener) = apply { this.listener = listener }
    }


    /**
     * Postback. Reply Button Clicked.
     *
     * - [text] - Button's display caption.
     * - [code] - Data associated with the Button.
     * - [mid] - Message ID of the button.
     * */
    interface Postback {
        val text: String
        val mid: Long
        val code: String
    }


    interface File {
        val id: String
        val fileName: String
        val type: String
        val size: Long
    }
}

