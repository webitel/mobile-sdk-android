package com.webitel.mobile_sdk.domain

interface TransferListener {

    /**
     * Called when data is transferred
     *
     */
    fun onData(value: ByteArray)

    /**
     * Called when transfer is paused
     *
     * @param pid process ID.
     */
    fun onPaused(pid: String)

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