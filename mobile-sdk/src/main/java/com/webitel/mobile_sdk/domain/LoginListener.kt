package com.webitel.mobile_sdk.domain


/**
 * Interface to handle login and logout events for a user session.
 */
interface LoginListener {

    /**
     * Called when the login process is completed successfully.
     *
     * @param session The user session created upon successful login.
     */
    fun onLoginFinished(session: Session)


    /**
     * Called when the logout process is completed successfully.
     * This is triggered when the user session is properly terminated.
     */
    fun onLogoutFinished()


    /**
     * Called when an error occurs during login or logout operations.
     *
     * @param e An Error object describing the issue that occurred.
     */
    fun onError(e: Error)
}