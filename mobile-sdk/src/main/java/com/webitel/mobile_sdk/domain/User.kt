package com.webitel.mobile_sdk.domain


data class User(
    /**
     * Issuer Identifier for the Issuer of the response.
     * The iss value is a case sensitive URL using the https scheme that contains scheme, host,
     * and optionally, port number and path components and no query or fragment components.
     * */
    val iss: String,


    /**
     * Subject Identifier.
     * A locally unique and never reassigned identifier within the Issuer for the End-User,
     * which is intended to be consumed by the Client, e.g., 24400320 or AItOawmwtWwcT0k51BayewNvutrJUqsvl6qs7A4.
     * It MUST NOT exceed 255 ASCII characters in length.
     * The sub value is a case sensitive string.
     * */
    val sub: String,


    /**
     * End-User's full name in displayable form including all name parts,
     * possibly including titles and suffixes, ordered according to the End-User's locale and preferences.
     * */
    val name: String
)