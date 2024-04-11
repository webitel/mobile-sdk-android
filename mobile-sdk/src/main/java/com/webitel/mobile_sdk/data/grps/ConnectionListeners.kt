package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.domain.ConnectListener
import com.webitel.mobile_sdk.domain.ConnectState


internal class ConnectionListeners {
    private val listeners = arrayListOf<ConnectListener>()


    fun onStateChanged(from: ConnectState, to: ConnectState) {
        listeners.forEach {
            it.onStateChanged(from = from, to = to)
        }
    }


    @Synchronized
    fun addListener(l: ConnectListener) {
        if (!listeners.contains(l)) {
            listeners.add(l)
        }
    }


    fun removeListener(l: ConnectListener?) {
        listeners.remove(l)
    }


    fun removeAllListeners() {
        listeners.clear()
    }
}