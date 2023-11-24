package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.Error
import webitel.portal.Connect.Response


internal class GrpcListeners {

    private val listeners = arrayListOf<GrpcListener>()


    fun onConnectionReady() {
        listeners.forEach {
            it.onConnectionReady()
        }
    }


    fun onConnectionError(e: Error) {
        listeners.forEach {
            it.onConnectionError(e)
        }
    }


    fun onResponse(response: Response) {
        listeners.forEach {
            it.onResponse(response)
        }
    }


    @Synchronized
    fun addListener(l: GrpcListener) {
        if (!listeners.contains(l)) {
            listeners.add(l)
        }
    }


    fun removeListener(l: GrpcListener) {
        listeners.remove(l)
    }


    fun removeAllListeners() {
        listeners.clear()
    }
}