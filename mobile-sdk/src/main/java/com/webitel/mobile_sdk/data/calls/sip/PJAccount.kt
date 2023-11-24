package com.webitel.mobile_sdk.data.calls.sip

import android.util.Log
import com.webitel.mobile_sdk.data.calls.sip.SipCallbacks
import com.webitel.mobile_sdk.data.calls.sip.SipConfig
import org.pjsip.pjsua2.Account
import org.pjsip.pjsua2.OnIncomingCallParam
import org.pjsip.pjsua2.OnRegStateParam
import java.lang.Exception

internal class PJAccount(
    private val callbacks: SipCallbacks,
    val transportUse: CallSettings.TransportUse,
    val config: SipConfig
): Account() {

    private var isRegistered: Boolean = false

    override fun onRegState(prm: OnRegStateParam?) {
        super.onRegState(prm)
        prm?.let {
            val code = it.code
            val expiration = it.expiration.toInt()
            try {
                val returnCode: Int = code
                if (returnCode / 100 == 2 && expiration != 0) {
                    if (!isRegistered) {
                        callbacks.onSipRegistrationSuccess()
                        isRegistered = true
                    }
                } else {
                    isRegistered = false
                    callbacks.onSipRegistrationFailure(
                        "Server returned code '$returnCode' while SIP registration")
                }
            } catch (e: Exception) {
                isRegistered = false
                callbacks.onSipRegistrationFailure(e.message)
            }
        }
    }

    fun isRegistered(): Boolean {
        return isRegistered
    }

    override fun onIncomingCall(prm: OnIncomingCallParam) {
        var wid = ""
        val msg  = prm.rdata.wholeMsg
        val index = msg.indexOf("X-Webitel-Uuid")
        if (index != -1) {
            val m = msg.substring(index)
            val s = m.split("\r\n").first().toString()
            wid = s.substring(s.indexOf(':')).trim(':', ' ')
            Log.e("vid", wid)
        }

        callbacks.onIncomingCallPJSIP(prm.callId, wid)
        Log.e("Incoming call", "======== Incoming call ======== \n" +
                "id - ${prm.callId}; wid - $wid")
    }
}