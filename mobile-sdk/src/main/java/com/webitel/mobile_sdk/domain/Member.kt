package com.webitel.mobile_sdk.domain


/**
 * Represents a member in the system, such as a user or participant in a chat.
 * Each member has an identifier, a name, and a type to distinguish between different roles or users.
 */
class Member(
    /**
     * Unique identifier for the member within the system.
     * This could represent a user ID or other unique identifier associated with the member.
     */
    val id: String,


    /**
     * The name of the member.
     * This is typically the full name or display name of the user/member in the system.
     */
    val name: String,


    /**
     * The type of member.
     */
    val type: String
)