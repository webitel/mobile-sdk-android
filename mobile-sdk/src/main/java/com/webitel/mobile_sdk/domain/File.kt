package com.webitel.mobile_sdk.domain


/**
 * Class representing a file with essential metadata attributes.
 *
 * @property id Unique identifier of the file.
 * @property fileName The name of the file, including its extension.
 * @property type The file type or MIME type, indicating the format (e.g., "image/png", "application/pdf").
 * @property size The file size in bytes.
 */
class File (
    val id: String,
    val fileName: String,
    val type: String,
    val size: Long
)