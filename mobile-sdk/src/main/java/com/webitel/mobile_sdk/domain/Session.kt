package com.webitel.mobile_sdk.domain


/**
 * Represents a user session in the application, encapsulating information about the user
 * and the availability of communication features like chat and voice.
 */
interface Session {

    /**
     * The user associated with this session.
     * Contains user-specific details such as ID, name, email, etc.
     */
    val user: User


    /**
     * Indicates whether the chat feature is available in the current session.
     * @return Boolean value - `true` if chat is available, `false` otherwise.
     */
    val isChatAvailable: Boolean
}
