package com.webitel.mobile_sdk.domain

/**
 * Data class representing an error response in the application.
 *
 * @property message A description of the error, providing details about what went wrong.
 * @property code The error code indicating the type or category of the error.
 * @property id Deprecated. Previously used as an identifier for the error instance.
 *             It is retained for backward compatibility but should not be used in new implementations.
 */
data class Error(
    val message: String,
    val code: Code,
    @Deprecated("Use other properties for error identification; 'id' is retained for backward compatibility only.")
    val id: String = ""
)


/**
 * Exception indicating an invalid state within the application.
 *
 * @param message A detailed message describing the invalid state that triggered the exception.
 */
class InvalidStateException(message: String) : Exception(message)