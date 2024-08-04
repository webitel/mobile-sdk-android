package com.webitel.mobile_sdk.domain


interface Dialog {
    val id: String
    val title: String

    fun getHistory(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getHistory(callback: CallbackListener<List<Message>>)

    fun getUpdates(request: HistoryRequest, callback: CallbackListener<List<Message>>)
    fun getUpdates(callback: CallbackListener<List<Message>>)

    fun sendMessage(message: Message.options, callback: MessageCallbackListener)


    @Deprecated(message = "Use 'downloadFile(fileId: String, listener: DownloadListener): CancellationToken' instead")
    fun downloadFile(fileId: String, observer: StreamObserver)


    /**
     * @param fileId File ID; Message.file,id.
     * @param listener listener for media downloading progress reporting.
     *
     * @return A CancellationToken that can be used to cancel the download operation.
     */
    fun downloadFile(fileId: String, listener: DownloadListener): CancellationToken


    /**
     * @param fileId File ID; Message.file,id.
     * @param offset Range: bytes=<start>.
     * @param listener listener for media downloading progress reporting.
     *
     * @return A CancellationToken that can be used to cancel the download operation.
     */
    fun downloadFile(fileId: String, offset: Long, listener: DownloadListener): CancellationToken


    /**
     * @param request request to send a file.
     * @param callback receive result  from server onSent(Message)/onError.
     *
     * @return A CancellationToken that can be used to cancel the sending operation.
     */
    fun sendFile(request: FileTransferRequest, callback: MessageCallbackListener): CancellationToken


    /**
     * @param mid Message ID of the button.
     * @param text Button's display caption.
     * @param code Data associated with the Button.
     * @param sendId ID of the sent request. You will receive this ID in message [DialogListener.onMessageAdd()]. Optional
     * @param callback receive result from server onSent/onError.
     */
    fun sendPostback(
        mid: Long,
        text: String,
        code: String,
        sendId: String? = null,
        callback: MessageCallbackListener
    )


    fun addListener(listener: DialogListener)
    fun removeListener(listener: DialogListener)
    fun removeAllListeners()
}