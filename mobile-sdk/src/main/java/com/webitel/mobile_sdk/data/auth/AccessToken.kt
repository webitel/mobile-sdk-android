package com.webitel.mobile_sdk.data.auth


internal data class AccessToken(
    val token: String,
    val expiresIn: Int
)