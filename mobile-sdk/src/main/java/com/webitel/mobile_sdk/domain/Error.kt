package com.webitel.mobile_sdk.domain


data class Error(
    val message: String,
    val code: Code,
    val id: String = ""
)