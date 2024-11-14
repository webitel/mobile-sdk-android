package com.webitel.mobile_sdk.domain


/**
 * A client interface for interacting with chat-related services.
 *
 * This interface defines methods for retrieving individual service dialogs and
 * lists of dialogs, providing callbacks for handling success and error cases.
 */
interface ChatClient {

    /**
     * Retrieves the current service dialog associated with the chat client.
     *
     * This method fetches a single `Dialog` instance that represents the
     * service dialog for the user. The result is delivered asynchronously via
     * the provided `CallbackListener`.
     *
     * @param callback A listener for receiving the result of the dialog request.
     *                 - On success: The `Dialog` instance is returned.
     *                 - On error: An error object is returned via the `onError` callback.
     */
    fun getServiceDialog(callback: CallbackListener<Dialog>)


    /**
     * Retrieves a list of dialogs associated with the chat client.
     *
     * This method asynchronously fetches a list of `Dialog` objects representing
     * the user's active chat dialogs. Results are provided via the
     * specified `CallbackListener`.
     *
     * @param callback A listener for handling the dialog list response.
     *                 - On success: A list of `Dialog` instances is returned.
     *                 - On error: An error object is returned via the `onError` callback.
     */
    fun getDialogs(callback: CallbackListener<List<Dialog>>)
}