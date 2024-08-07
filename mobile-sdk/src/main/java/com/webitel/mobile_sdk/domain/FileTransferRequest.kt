package com.webitel.mobile_sdk.domain

import java.io.InputStream


/**
 * Represents a request for transferring a file.
 */
class FileTransferRequest private constructor(
    val stream: InputStream,
    val fileName: String,
    val mimeType: String,
    val pid: String?,
    val listener: UploadListener?) {


    /**
     * A builder for creating FileTransferRequest instances.
     * Initializes a new Builder with the required parameters.
     *
     * @param stream The input stream of the file to be transferred.
     * @param fileName The name of the file.
     * @param mimeType The MIME type of the file.
     */
    class Builder(
        private val stream: InputStream,
        private val fileName: String,
        private val mimeType: String
    ) {


        /**
         * Convenience initializer for creating a Builder with a process ID (pid).
         *
         * @param stream The input stream of the file to be transferred.
         * @param pid The process ID for resuming a transfer.
         */
        constructor(stream: InputStream, pid: String):
                this(stream = stream, fileName = "", mimeType = "") {
            this.pid = pid
        }

        private var listener: UploadListener? = null
        private var pid: String? = null


        /**
         * Sets the upload listener.
         *
         * @param listener: The listener for upload events.
         *
         * @return The Builder instance for chaining.
         */
        fun transferListener(listener: UploadListener) = apply { this.listener = listener }


        /**
         * Builds and returns a FileTransferRequest instance.
         *
         * @return A configured FileTransferRequest instance.
         */
        fun build(): FileTransferRequest {
            return FileTransferRequest(stream, fileName, mimeType, pid, listener)
        }
    }
}