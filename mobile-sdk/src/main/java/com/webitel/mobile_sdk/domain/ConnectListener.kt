package com.webitel.mobile_sdk.domain


interface ConnectListener {
    fun onStateChanged(from: ConnectState, to: ConnectState)
}