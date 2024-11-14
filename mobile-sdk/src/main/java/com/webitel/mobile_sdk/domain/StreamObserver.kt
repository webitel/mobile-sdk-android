package com.webitel.mobile_sdk.domain


/**
 * This interface is used to observe the progress of a file download operation.
 * It provides methods to handle different stages of the download process:
 * - onNext: Called to deliver chunks of data.
 * - onError: Called when an error occurs during the download.
 * - onCompleted: Called when the download is successfully completed.
 */
interface StreamObserver {

    /**
     * Called when a chunk of data (file part) has been received during the download.
     * @param value A byte array representing the received chunk of data.
     */
    fun onNext(value: ByteArray)


    /**
     * Called when an error occurs during the download operation.
     * @param e The error that occurred during the download.
     */
    fun onError(e: Error)


    /**
     * Called when the file download has been completed successfully.
     * This method indicates that all data has been received and no more chunks are expected.
     */
    fun onCompleted()
}