package com.webitel.mobile_sdk.domain


data class Error(
    val message: String,
    val code: Code,
    val id: String = ""
)


class InvalidStateException(message: String) : Exception(message)
class InvalidProcessIdException(message: String) : Exception(message)