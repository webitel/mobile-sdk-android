package com.webitel.mobile_sdk.domain


/**
 * Represents a request to retrieve a historical set of messages or events, with parameters for pagination.
 *
 * @property offset Optional ID of the message from which to start retrieving historical messages.
 * This allows fetching history starting from a specific message in the conversation's timeline.
 * @property limit The maximum number of items to retrieve in one request, with a default value of 50.
 */
class HistoryRequest private constructor(builder: Builder){
    val offset: Long?
    val limit: Int

    init {
        offset = builder.offset
        limit = builder.limit
    }


    /**
     * Builder class for constructing a HistoryRequest with customized pagination settings.
     *
     * @property offset The ID of the starting message to retrieve data from.
     * @property limit The number of items to retrieve, which can be customized by chaining methods.
     */
    class Builder {
        var offset: Long? = null
            private set
        var limit: Int = 50
            private set


        /**
         * Sets the offset for the history request, specifying the message ID from which
         * to start retrieving historical data.
         *
         * @param offset The message ID to use as the starting point in the history.
         * @return The Builder instance with the updated offset.
         */
        fun offset(offset: Long) = apply { this.offset = offset }


        /**
         * Sets the limit for the number of messages to retrieve in the history request.
         *
         * @param limit The maximum number of items to retrieve in one request.
         * @return The Builder instance with the updated limit.
         */
        fun limit(limit: Int) = apply { this.limit = limit }


        /**
         * Builds and returns the HistoryRequest instance with the specified parameters.
         *
         * @return A configured HistoryRequest instance.
         */
        fun build() = HistoryRequest(this)
    }
}