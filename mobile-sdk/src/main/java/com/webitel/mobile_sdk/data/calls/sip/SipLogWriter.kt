package com.webitel.mobile_sdk.data.calls.sip

import android.util.Log
import org.pjsip.pjsua2.LogEntry
import org.pjsip.pjsua2.LogWriter

internal class SipLogWriter : LogWriter() {
    override fun write(entry: LogEntry?) {
        Log.d("sip", javaClass.simpleName + " " + (entry?.msg ?: "empty"))
    }
}