package com.webitel.mobile_sdk.domain


/**
 * Represents a message in the system.
 * Includes properties for message content, sender, timestamp, and additional message options.
 */
interface Message {

    /**
     * Unique identifier for the message within the chat.
     */
    val id: Long


    /**
     * Client-generated request ID for the message.
     * This ID is used to track the message request from the client side.
     */
    val sendId: String


    /**
     * The text content of the message.
     * This can be null if the message does not contain text, for example, when it's a file or postback message.
     */
    val text: String?


    /**
     * The media or attachment associated with the message, if any.
     * This can be null if the message does not contain a file.
     */
    val file: File?


    /**
     * The sender of the message.
     * This represents the user or member who sent the message.
     */
    val from: Member


    /**
     * Indicates whether the message is incoming or outgoing.
     * A message is considered incoming if it was received by the client.
     */
    val isIncoming: Boolean


    /**
     * Timestamp indicating when the message was sent or published.
     * This is represented in milliseconds since the Unix epoch.
     */
    val sentAt: Long


    /**
     * Optional custom message type classifier. Default: empty string.
     *
     * May be used by the client UI to extend the standard set of message types
     * (e.g., "text", "file") with custom system or service-specific types.
     *
     * Examples:
     * - "queue_update"
     * - "system_alert"
     */
    val kind: String


    /**
     * Represents the postback, which occurs when a reply button is clicked.
     * This is used for handling button click actions within the message.
     * This can be null if the message does not have a postback associated with it.
     */
    val postback: Postback?


    /**
     * Represents the reply markup (buttons) attached to the message.
     * This can be null if the message does not have any buttons or interactive elements.
     */
    val replyMarkup: ReplyMarkup?


    /**
     * Options for constructing a message.
     * Provides a way to specify text, send ID, and file attachment for a message.
     */
    class options {
        internal var text: String? = null
            private set

        internal var sendId: String? = null
            private set

        internal var file: File? = null
            private set

        internal var kind: String? = null
            private set


        /**
         * Sets the send ID for the message.
         * This is used for tracking and identifying the message request.
         */
        fun sendId(id: String) = apply { this.sendId = id }


        /**
         * Sets a custom type classifier for the message.
         *
         * Allows extending standard types (like "text" or "file") with custom
         * UI-specific or system-level classifications.
         *
         * @param kind the custom type string (e.g., "system_alert", "queue_update")
         * @return the updated message options.
         */
        fun kind(kind: String) = apply { this.kind = kind }


        /**
         * Sets the text content for the message.
         */
        fun withText(text: String) = apply { this.text = text }


        /**
         * Attaches a file to the message.
         */
        fun withFile(file: File) = apply { this.file = file }
    }



    /**
     * Represents a postback, which occurs when a reply button is clicked within the message.
     * Contains information about the button clicked, including the button's display text, associated code, and the message ID.
     */
    interface Postback {

        /**
         * The display caption of the button
         */
        val text: String


        /**
         * The message ID associated with the button
         */
        val mid: Long


        /**
         * The data associated with the button, typically used to identify the action or event triggered
         */
        val code: String
    }
}

