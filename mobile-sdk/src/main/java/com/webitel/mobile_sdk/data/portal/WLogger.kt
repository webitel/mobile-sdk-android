package com.webitel.mobile_sdk.data.portal

import android.util.Log
import com.webitel.mobile_sdk.domain.LogLevel

internal class WLogger(private val level: LogLevel) {

    fun info(tag: String, message: String) {
        if (level <= LogLevel.INFO) {
            Log.i(tag, message)
        }
    }

    fun debug(tag: String, message: String) {
        if (level <= LogLevel.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun error(tag: String, message: String) {
        if (level <= LogLevel.ERROR) {
            Log.e(tag, message)
        }
    }
}