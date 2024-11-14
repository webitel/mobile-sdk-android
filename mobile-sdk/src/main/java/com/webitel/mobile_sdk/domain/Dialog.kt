package com.webitel.mobile_sdk.domain


/**
 * Represents a dialog interface, providing methods to retrieve conversation history,
 * manage file transfers, and interact with messages.
 */
interface Dialog {

    /**
     * Unique identifier for this dialog.
     */
    val id: String


    /**
     * Title or name associated with this dialog.
     */
    val title: String


    /**
     * Retrieves the message history for this dialog based on a request.
     *
     * @param request The history request parameters.
     * @param callback Callback to receive a list of messages or an error.
     */
    fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>)


    /**
     * Retrieves the history of the last 50 messages.
     *
     * @param callback Callback to receive a list of messages or an error.
     */
    fun getHistory(callback: CallbackListener<List<Message>>)


    /**
     * Fetches recent updates to the message history for this dialog.
     *
     * @param request The update request parameters, such as limit and offset.
     * @param callback Callback to receive updated messages or an error.
     */
    fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>)


    /**
     * Fetches recent updates to the message history for this dialog
     *
     * @param callback Callback to receive updated messages or an error.
     */
    fun getUpdates(callback: CallbackListener<List<Message>>)


    /**
     * Sends a message within this dialog.
     *
     * @param message The message details including text, media, or other options.
     * @param callback Callback to handle message sending status and any errors.
     */
    fun sendMessage(message: Message.options, callback: MessageCallbackListener)


    /**
     * Deprecated. Use `downloadFile(fileId: String, listener: DownloadListener)` instead.
     *
     * Downloads a file associated with this dialog.
     *
     * @param fileId The unique identifier for the file to download.
     * @param observer Observes the download stream, for real-time data updates.
     */
    @Deprecated(message = "Use 'downloadFile(fileId: String, listener: DownloadListener): CancellationToken' instead")
    fun downloadFile(fileId: String, observer: StreamObserver)


    /**
     * Initiates a file download with progress reporting.
     *
     * @param fileId The unique identifier for the file.
     * @param listener Listener to report download progress and completion.
     *
     * @return A `CancellationToken` to cancel the download operation if needed.
     */
    fun downloadFile(fileId: String, listener: DownloadListener): CancellationToken


    /**
     * Initiates a file download from a specific byte offset, useful for resuming downloads.
     *
     * @param fileId The unique identifier for the file.
     * @param offset Starting byte offset for the download.
     * @param listener Listener to report download progress and completion.
     *
     * @return A `CancellationToken` to cancel the download operation if needed.
     */
    fun downloadFile(fileId: String, offset: Long, listener: DownloadListener): CancellationToken


    /**
     * Uploads a file to this dialog with progress reporting.
     *
     * @param request Contains file data and metadata for the upload.
     * @param callback Callback to handle upload results, including metadata and hash.
     *
     * @return A `CancellationToken` to cancel the upload operation if needed.
     */
    fun uploadFile(request: FileTransferRequest, callback: CallbackListener<UploadResult>): CancellationToken


    /**
     * Sends a postback button response within a message.
     *
     * @param mid The message ID that contains the button.
     * @param text Text displayed on the button.
     * @param code Data associated with the button for processing.
     * @param sendId Optional identifier for the send operation; will be returned in the
     *               [DialogListener.onMessageAdd()] callback.
     * @param callback Callback to handle result of sending the postback.
     */
    fun sendPostback(
        mid: Long,
        text: String,
        code: String,
        sendId: String? = null,
        callback: MessageCallbackListener
    )


    /**
     * Adds a listener to receive events related to this dialog.
     *
     * @param listener The listener to add.
     */
    fun addListener(listener: DialogListener)


    /**
     * Removes a previously added listener from this dialog.
     *
     * @param listener The listener to remove.
     */
    fun removeListener(listener: DialogListener)


    /**
     * Removes all listeners from this dialog.
     */
    fun removeAllListeners()
}