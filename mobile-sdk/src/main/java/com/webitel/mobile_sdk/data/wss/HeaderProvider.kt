package com.webitel.mobile_sdk.data.wss

import com.webitel.mobile_sdk.data.grps.ChannelConfig


internal class HeaderProvider(private val config: ChannelConfig) {
    @Volatile
    private var accessToken: String? = null

    fun updateAccessToken(token: String?) {
        accessToken = token
    }

    fun commonHeaders(): Map<String, String> {
        val map = mutableMapOf(
            "x-portal-device" to config.deviceId,
            "x-portal-client" to config.clientToken,
            "User-Agent" to config.agent
        )

        accessToken?.takeIf { it.isNotBlank() }?.let {
            map["x-portal-access"] = it
        }

        return map
    }
}