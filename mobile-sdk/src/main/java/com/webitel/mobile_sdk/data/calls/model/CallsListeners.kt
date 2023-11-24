package com.webitel.mobile_sdk.data.calls.model

import com.webitel.mobile_sdk.domain.Call
import com.webitel.mobile_sdk.domain.CallState
import com.webitel.mobile_sdk.domain.CallStateListener
import com.webitel.mobile_sdk.domain.Error


internal class CallsListeners {
    private val listeners = arrayListOf<CallStateListener>()

    fun onCallStateChanged(call: Call, oldState: List<CallState>) {
        listeners.forEach {
            it.onCallStateChanged(call, oldState)
        }
    }


    fun onCreateCallFailed(e: Error) {
        listeners.forEach {
            it.onCreateCallFailed(e)
        }
    }


    @Synchronized
    fun addListener(l: CallStateListener) {
        if (!listeners.contains(l)) {
            listeners.add(l)
        }
    }


    fun removeListener(l: CallStateListener?) {
        listeners.remove(l)
    }


    fun removeAllListeners() {
        listeners.clear()
    }
}