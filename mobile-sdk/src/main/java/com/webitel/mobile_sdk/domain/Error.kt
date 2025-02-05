package com.webitel.mobile_sdk.domain

/**
 * Data class representing an error response in the application.
 *
 * @property message A description of the error, providing details about what went wrong.
 * @property code The error code indicating the type or category of the error.
 */
data class Error(
    override val message: String,
    val code: Code
): Throwable()


/**
 * Exception indicating an invalid state within the application.
 *
 * @param message A detailed message describing the invalid state that triggered the exception.
 */
class InvalidStateException(message: String) : Exception(message)