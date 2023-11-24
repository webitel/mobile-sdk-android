package com.webitel.mobile_sdk.domain


class HistoryRequest private constructor(builder: Builder){
    val offset: Long?
    val limit: Int

    init {
        offset = builder.offset
        limit = builder.limit
    }


    class Builder {
        var offset: Long? = null
            private set
        var limit: Int = 50
            private set

        fun offset(offset: Long) = apply { this.offset = offset }
        fun limit(limit: Int) = apply { this.limit = limit }
        fun build() = HistoryRequest(this)
    }
}