package com.webitel.mobile_sdk.data.wss

import com.webitel.mobile_sdk.domain.Code

internal object ErrorCodeMapper {

    fun fromHttpStatus(status: Int): Code {
        return when (status) {
            400 -> Code.FAILED_PRECONDITION
            401 -> Code.UNAUTHENTICATED
            403 -> Code.PERMISSION_DENIED
            404 -> Code.NOT_FOUND
            409 -> Code.ALREADY_EXISTS
            415 -> Code.UNSUPPORTED_MEDIA_TYPE
            429 -> Code.RESOURCE_EXHAUSTED
            499 -> Code.CANCELLED
            500 -> Code.INTERNAL
            501 -> Code.UNIMPLEMENTED
            503 -> Code.UNAVAILABLE
            504 -> Code.DEADLINE_EXCEEDED
            else -> Code.UNKNOWN
        }
    }
}