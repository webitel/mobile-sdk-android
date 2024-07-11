package com.webitel.mobile_sdk.domain

interface UploadListener {
    /**
     * A callback invoked to report upload progress, receiving an Long parameter representing the number of bytes uploaded.
     *
     * @param size Total size of saved data.
     */
    fun onProgress(size: Long)

    /**
     * A callback invoked when the upload starts, receiving a String parameter representing the processId (pid).
     *
     * @param pid processId
     */
    fun onStarted(pid: String)

    /**
     * A callback invoked when the upload is completed.
     *
     */
    fun onCompleted()

    /**
     * A callback invoked when the upload is canceled.
     *
     */
    fun onCanceled()

    /**
     * Called when an error occurred during data transfer
     *
     */
    fun onError(err: Error)
}