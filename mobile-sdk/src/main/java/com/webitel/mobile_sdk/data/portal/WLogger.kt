package com.webitel.mobile_sdk.data.portal

import android.util.Log
import com.webitel.mobile_sdk.domain.LogLevel

internal class WLogger {
    var level: LogLevel = LogLevel.ERROR
    private val pref = "webitel:"
    fun info(tag: String, message: String) {
        if (level <= LogLevel.INFO) {
            Log.i(pref+tag, message)
        }
    }

    fun debug(tag: String, message: String) {
        if (level <= LogLevel.DEBUG) {
            Log.d(pref+tag, message)
        }
    }

    fun warn(tag: String, message: String) {
        if (level <= LogLevel.WARN) {
            Log.w(pref+tag, message)
        }
    }

    fun error(tag: String, message: String) {
        if (level <= LogLevel.ERROR) {
            Log.e(pref+tag, message)
        }
    }
}