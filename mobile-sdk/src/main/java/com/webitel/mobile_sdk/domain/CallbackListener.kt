package com.webitel.mobile_sdk.domain


/**
 * A generic callback interface for handling asynchronous results.
 * Provides methods to manage success and error outcomes.
 *
 * @param T The type of result expected upon successful completion.
 */
interface CallbackListener<T> {

    /**
     * Called when the operation completes successfully.
     *
     * @param t The result of type T obtained from the operation.
     */
    fun onSuccess(t: T)

    /**
     * Called when the operation fails.
     *
     * @param e An Error object containing information about the failure.
     */
    fun onError(e: Error)
}