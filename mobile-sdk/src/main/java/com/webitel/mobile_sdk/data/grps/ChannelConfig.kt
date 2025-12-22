package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.Transport

internal data class ChannelConfig(
    val host: String,
    val port: Int,
    val agent: String,
    val clientToken: String,
    val deviceId: String,
    val keepAliveTime: Long,
    val keepAliveTimeout: Long,
    val scheme: String,
    val transport: Transport
)