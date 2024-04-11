package com.webitel.mobile_sdk.data.grps

import io.grpc.Metadata
import io.grpc.Status

internal interface StreamListener {

    fun onStart(methodName: String)

    fun onReady(methodName: String)

    fun onClose(methodName: String, status: Status?, trailers: Metadata?)
}