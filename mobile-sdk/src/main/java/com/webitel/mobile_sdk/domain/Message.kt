package com.webitel.mobile_sdk.domain


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

        internal var file: File? = null
            private set

        fun sendId(id: String) = apply { this.sendId = id }

        fun withText(text: String) = apply { this.text = text }

        fun withFile(file: File) = apply { this.file = file }
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
}

