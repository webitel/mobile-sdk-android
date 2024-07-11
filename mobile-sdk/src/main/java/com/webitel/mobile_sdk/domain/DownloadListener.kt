package com.webitel.mobile_sdk.domain

interface DownloadListener {
    /**
     * Called when data is transferred
     *
     */
    fun onData(value: ByteArray)

    /**
     * Called when transfer is complete
     *
     */
    fun onCompleted()

    /**
     * Called when transfer is canceled
     *
     */
    fun onCanceled()

    /**
     * Called when an error occurred during data transfer
     *
     */
    fun onError(err: Error)
}