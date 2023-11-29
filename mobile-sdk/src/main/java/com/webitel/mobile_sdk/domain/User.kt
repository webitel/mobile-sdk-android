package com.webitel.mobile_sdk.domain


class User private constructor(builder: Builder) {

    val iss: String
    val sub: String
    val name: String
    val email: String
    val emailVerified: Boolean
    val phoneNumber: String
    val phoneNumberVerified: Boolean
    var locale: String

    init {
        iss = builder.iss
        sub = builder.sub
        name = builder.name
        email = builder.email
        emailVerified = builder.emailVerified
        phoneNumber = builder.phoneNumber
        phoneNumberVerified = builder.phoneNumberVerified
        locale = builder.locale
    }


    class Builder(
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
    ) {
        var email: String = ""
            private set
        var emailVerified: Boolean = false
            private set

        var phoneNumber: String = ""
            private set
        var phoneNumberVerified: Boolean = false
            private set

        var locale: String = ""
            private set

        fun email(value: String) = apply { this.email = value }

        fun emailVerified(value: Boolean) = apply { this.emailVerified = value }

        fun phoneNumber(value: String) = apply { this.phoneNumber = value }

        fun phoneNumberVerified(value: Boolean) = apply { this.phoneNumberVerified = value }

        /**
         *  End-User's locale, represented as a BCP47 [RFC5646] language tag.
         *  This is typically an ISO 639-1 Alpha-2 [ISO639‑1] language code in lowercase
         *  and an ISO 3166-1 Alpha-2 [ISO3166‑1] country code in uppercase,
         *  separated by a dash. For example, `en-US` or `uk-UA`.
         */
        fun locale(value: String) = apply { this.locale = value }

        fun build() = User(this)
    }
}