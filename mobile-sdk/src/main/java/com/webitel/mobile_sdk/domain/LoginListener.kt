package com.webitel.mobile_sdk.domain

interface LoginListener {
    fun onLoginFinished(session: Session)
    fun onLogoutFinished()
    fun onError(e: Error)
}