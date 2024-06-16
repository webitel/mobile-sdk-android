package com.webitel.mobile_sdk.domain

interface TransferControl {
    /**
     * Process ID.
     */
    val pid: String

    /**
     * Pauses the file transfer.
     *
     * @throws InvalidStateException if the transfer is already completed or canceled.
     */
    @Throws(InvalidStateException::class)
    fun pause()

    /**
     * Resumes the file transfer if it is not already completed or canceled.
     *
     * @throws InvalidStateException if the transfer is already completed or canceled.
     */
    @Throws(InvalidStateException::class)
    fun resume()

    /**
     * Cancels the file transfer.
     *
     * @throws InvalidStateException if the transfer is already completed or canceled.
     */
    @Throws(InvalidStateException::class)
    fun cancel()
}