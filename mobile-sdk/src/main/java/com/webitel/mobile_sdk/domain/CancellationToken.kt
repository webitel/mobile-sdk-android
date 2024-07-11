package com.webitel.mobile_sdk.domain

interface CancellationToken {

    /**
     * Cancels the file transfer.
     *
     * @param cleanUp A boolean flag indicating whether the data already sent to the server
     *     should be deleted and the processID (pid) invalidated when the transfer is cancelled.
     *     By default cleanUp = false
     *
     *  @throws InvalidStateException if the transfer is already completed or canceled.
     */
    @Throws(InvalidStateException::class)
    fun cancel(cleanUp: Boolean)


    /**
     * Cancels the file transfer.
     *
     * @throws InvalidStateException if the transfer is already completed or canceled.
     */
    @Throws(InvalidStateException::class)
    fun cancel()
}