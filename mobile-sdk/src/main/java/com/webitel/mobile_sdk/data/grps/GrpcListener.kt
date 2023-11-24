package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.Error
import webitel.portal.Connect

internal interface GrpcListener {
    fun onResponse(response: Connect.Response)
    fun onConnectionError(e: Error)
    fun onConnectionReady()
}