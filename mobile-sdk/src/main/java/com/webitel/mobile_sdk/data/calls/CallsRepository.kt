package com.webitel.mobile_sdk.data.calls

import android.content.ComponentName
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.CallAudioState
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import com.webitel.mobile_sdk.data.calls.common.Constants
import com.webitel.mobile_sdk.data.calls.model.WebitelCall
import com.webitel.mobile_sdk.data.calls.sip.CallSettings
import com.webitel.mobile_sdk.data.calls.sip.SipCallbacks
import com.webitel.mobile_sdk.data.calls.sip.SipConfig
import com.webitel.mobile_sdk.data.calls.sip.SipManager
import com.webitel.mobile_sdk.data.calls.telecom.CallConnection
import com.webitel.mobile_sdk.data.calls.telecom.CallsService4
import com.webitel.mobile_sdk.data.LibraryModule
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import java.util.Timer
import java.util.TimerTask


internal class CallsRepository private constructor() {
    private val WAITING_TO_CALL_MILLIS: Long = 5000
    private val PHONE_ACCOUNT_HANDLE_ID = "Webitel2"

    private val handler = WebitelHandler()
    private val phoneAccountHandle = PhoneAccountHandle(
        ComponentName(LibraryModule.application, CallsService4::class.java),
        PHONE_ACCOUNT_HANDLE_ID
    )
    private val audioManager = LibraryModule.application
        .getSystemService(Context.AUDIO_SERVICE) as AudioManager?


    private val sipManager: SipManager = SipManager(object : SipCallbacks {
        override fun onSipRegistrationSuccess() {
            activeCalls.forEach { (_, call) ->
                if (call.isInbound) {
                    call.finishTimer = Timer()
                    call.finishTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            activeCalls.remove(call.id)
                            call.destroyCall()
                        }
                    }, WAITING_TO_CALL_MILLIS)
                } else {
                    createSipConnection(call.id)
                }
            }
        }

        override fun onSipRegistrationFailure(errorMessage: String?) {
            activeCalls.forEach { (_, call) ->
                call.destroyCall()
            }
            activeCalls.clear()
        }

        override fun onIncomingCallPJSIP(callId: Int, webitelId: String) {}
    })


    fun makeCall(call: WebitelCall) {
        val outgoingUri: Uri =
            Uri.fromParts(PhoneAccount.SCHEME_SIP, "00", null)

        val outgoingExtras = Bundle()
        outgoingExtras.putString(PARAM_CALL_ID, call.id)
        outgoingExtras.putParcelable(
            TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle
        )
        outgoingExtras.putBundle(
            TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, Bundle(outgoingExtras)
        )

        holdAllCalls()
        activeCalls[call.id] = call

        try {
            val telecomManager: TelecomManager =
                LibraryModule.application.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            registerPhoneAccount(telecomManager)
            telecomManager.placeCall(outgoingUri, outgoingExtras)

        } catch (e: SecurityException) {
            call.onCreateCallFailed(
                Error(
                    message = e.message.toString(),
                    code = Code.PERMISSION_DENIED
                )
            )

        } catch (e: Exception) {
            call.onCreateCallFailed(
                Error(
                    message = e.message.toString(),
                    code = Code.UNKNOWN
                )
            )
        }
    }


    fun holdOtherCalls(id: String) {
        activeCalls.forEach { (_, v) ->
            if (v.id != id) {
                v.setHold()
            }
        }
    }


    fun deleteCall(id: String) {
        activeCalls.remove(id)
    }


    fun getActiveAudioRoute(): Int {
        return when {
            isSpeakerOn -> {
                CallAudioState.ROUTE_SPEAKER
            }

            isBluetoothHeadsetConnected() -> {
                CallAudioState.ROUTE_BLUETOOTH
            }

            else -> {
                CallAudioState.ROUTE_WIRED_OR_EARPIECE
            }
        }
    }


    fun make(job: Runnable) {
        handler.make(job)
    }


    fun onDestroyTelecomService() {
        make {
            stopStack()
        }
    }


    fun checkPjsipThread() {
        sipManager.checkThread()
    }


    fun makeSipCallFor(call: WebitelCall) {
        sipManager.checkThread()
        if (sipManager.isDestroying()) sipManager.quickDestroy()

        if (!sipManager.isAccountCreated()) {
            call.setRegisterAccountState()
            registerSip(call.config)
        }

        val connection = sipManager.newOutgoingCall(
            callback = call,
            number = call.toNumber,
            name = call.toName
        )
        if (connection != null)
            call.setSipConnection(connection)

    }


    private fun isBluetoothHeadsetConnected(): Boolean {
        val devices = audioManager?.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        var isConnected = false
        devices?.forEach {
            if (it.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) isConnected = true
        }
        return isConnected
    }


    private fun stopStack() {
        isSpeakerOn = false
        if (activeCalls.isNotEmpty()) {
            activeCalls.forEach { (_, call) ->
                call.destroyCall()
            }
        }
        activeCalls.clear()
        sipManager.stopStack()

    }


    private fun holdAllCalls() {
        activeCalls.forEach { (_, v) ->
            v.setHold()
        }
    }


    private fun registerSip(config: SipConfig) {
        createAccount(CallSettings(), config)
    }


    private fun createAccount(settings: CallSettings, config: SipConfig) {
        sipManager.createAccount(settings, config)
    }


    private fun registerPhoneAccount(telecomManager: TelecomManager) {
        val builder = PhoneAccount.builder(phoneAccountHandle, PHONE_ACCOUNT_HANDLE_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
        }
        builder.addSupportedUriScheme("sip")
        val phoneAccount = builder.build()
        telecomManager.registerPhoneAccount(phoneAccount)
    }


    private fun createSipConnection(id: String) {
        val c = activeCalls[id] ?: return
        val connection = sipManager.newOutgoingCall(
            callback = c,
            number = c.toNumber,
            name = c.toName
        )
        if (connection != null) {
            c.setSipConnection(connection)
        } else {
            c.destroyCall()
        }
    }


    private fun loadNativeLibraries() {
        try {
            System.loadLibrary("c++_shared")
            Log.d("loadNativeLibraries", "libc++_shared loaded")
        } catch (error: UnsatisfiedLinkError) {
            Log.e(
                "loadNativeLibraries",
                "Error while loading libc++_shared native library",
                error
            )
        }
//        try {
//            System.loadLibrary("openh264")
//            Log.d(TAG, "OpenH264 loaded")
//        } catch (error: UnsatisfiedLinkError) {
//            Log.e(TAG, "Error while loading OpenH264 native library", error)
//        }
        try {
            System.loadLibrary("pjsua2")
            Log.d("loadNativeLibraries", "PJSIP pjsua2 loaded")
        } catch (error: UnsatisfiedLinkError) {
            Log.e(
                "loadNativeLibraries",
                "Error while loading PJSIP pjsua2 native library",
                error
            )
        }
    }


    internal companion object : Constants {
        val instance = CallsRepository()
        var activeCalls: HashMap<String, WebitelCall> = HashMap()
        var isSpeakerOn: Boolean = false


        fun getCallById(id: String): WebitelCall? {
            return activeCalls[id]
        }


        fun getActiveConnections(): List<CallConnection> {
            return ArrayList(activeCalls.mapNotNull { value -> value.value.getConnection() })
        }
    }
}




