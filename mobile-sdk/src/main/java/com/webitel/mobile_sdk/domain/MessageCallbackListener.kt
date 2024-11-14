package com.webitel.mobile_sdk.domain


/**
 * Listener for handling the result of sending a message.
 */
interface MessageCallbackListener {

    /**
     * Called when the message has been successfully sent.
     *
     * @param m The message object that was sent.
     */
    fun onSent(m: Message)


    /**
     * Called when an error occurs while sending the message.
     *
     * @param e The error object that describes the failure.
     */
    fun onError(e: Error)
}