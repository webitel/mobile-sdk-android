package com.webitel.mobile_sdk.data.auth.storage

import com.webitel.mobile_sdk.data.auth.AccessToken


internal interface AuthStorage {
    fun getAccessToken(): AccessToken?

    fun saveAccessToken(token: AccessToken)

    fun clear()
}