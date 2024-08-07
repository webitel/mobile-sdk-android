package com.webitel.mobile_sdk.domain

/**
 * Represents the result of an upload operation, containing the saved document metadata and hashsum variants.
 */
data class UploadResult(
    /**
     * Metadata of the saved document.
     */
    val file: File,

    /**
     * Map containing hashsum variants of stored data. The key is the algorithm name and the value is the corresponding hash.
     */
    val hash: Map<String, String>
)
