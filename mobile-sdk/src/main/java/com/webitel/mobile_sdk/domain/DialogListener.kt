package com.webitel.mobile_sdk.domain


/**
 * Interface for listening to dialog events, such as message or member changes,
 * to update the client-side view of a dialog in real time.
 */
interface DialogListener {

    /**
     * Called when a new message is added to the dialog.
     *
     * @param message The message that was added, including content, sender details,
     *                and timestamp.
     */
    fun onMessageAdded(message: Message)
//    fun onMessageUpdated(message: Message)
//    fun onMessageDeleted(message: Message)
//    fun onMemberAdded(member: Member)
//    fun onMemberDeleted(member: Member)
}