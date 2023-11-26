package com.webitel.mobile_sdk.data.calls.telecom

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.telecom.Call
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import com.webitel.mobile_sdk.data.calls.CallsRepository
import com.webitel.mobile_sdk.data.calls.common.Constants
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error

class CallsService4: ConnectionService(), Constants {

    private var mWakeLock: PowerManager.WakeLock? = null

    private val c = CallsRepository.activeCalls

    override fun onCreate() {
        super.onCreate()
        acquireWakeLock()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_LOGOUT_ACCOUNT -> logout()
        }
        return START_NOT_STICKY
    }



    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection? {
        val extras = request!!.extras


        val idParam = extras.getString(PARAM_CALL_ID) ?: return null

        val call = CallsRepository.getCallById(idParam)
            ?: return null

        val outgoingUri: Uri =
            Uri.fromParts(PhoneAccount.SCHEME_SIP, call.toNumber, null)
        val connection = CallConnection(call)
        connection.setInitializing()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            connection.connectionProperties = Call.Details.PROPERTY_SELF_MANAGED
        }

        connection.number = call.toNumber
        connection.displayName = call.toName
        connection.isInbound = true
        connection.connectionCapabilities = Connection.CAPABILITY_MUTE and Connection.CAPABILITY_SUPPORT_HOLD
        connection.setConferenceableConnections(CallsRepository.getActiveConnections())
        connection.audioModeIsVoip = true
        connection.videoState = VideoProfile.STATE_AUDIO_ONLY
        connection.setCallerDisplayName(call.toName, TelecomManager.PRESENTATION_ALLOWED)
        connection.setAddress(outgoingUri, TelecomManager.PRESENTATION_ALLOWED)

        call.setTelecomCall(connection)

        return connection
    }


    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection? {
        val extras = request!!.extras
        val idParam = extras.getString(PARAM_CALL_ID) ?: return null
        val call = CallsRepository.getCallById(idParam)
            ?: return null

        val connection = CallConnection(call)
        connection.setInitializing()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            connection.connectionProperties = Call.Details.PROPERTY_SELF_MANAGED
        }

        connection.number = call.toNumber
        connection.displayName = call.toName
        connection.audioModeIsVoip = true
        connection.videoState = VideoProfile.STATE_AUDIO_ONLY
        connection.connectionCapabilities = Connection.CAPABILITY_MUTE and Connection.CAPABILITY_SUPPORT_HOLD
        connection.setCallerDisplayName(call.toNumber, TelecomManager.PRESENTATION_ALLOWED)
        connection.setAddress(request.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setConferenceableConnections(CallsRepository.getActiveConnections())

        call.setTelecomCall(connection)
        call.makeSipCall()

        return connection
    }




    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        val idParam = request?.extras?.getString(PARAM_CALL_ID)
        val call = CallsRepository.getCallById(idParam ?: "null")

        call?.onCreateCallFailed(
            Error(
                message = "Failed to create outgoing call",
                code = Code.UNKNOWN
            )
        )

        super.onCreateOutgoingConnectionFailed(
            connectionManagerPhoneAccount,
            request
        )
    }


    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {

        val idParam = request?.extras?.getString(PARAM_CALL_ID)
        val call = CallsRepository.getCallById(idParam ?: "null")

        call?.onCreateCallFailed(
            Error(
                message = "Failed to create incoming call",
                code = Code.UNKNOWN
            )
        )

        super.onCreateIncomingConnectionFailed(
            connectionManagerPhoneAccount,
            request
        )
    }


    override fun onDestroy() {
        releaseWakeLock()
        CallsRepository.instance.onDestroyTelecomService()
        super.onDestroy()
    }


    private fun logout() {
        destroy()
    }


    private fun destroy() {
        stopSelf()
    }


    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(
            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
            javaClass.simpleName
        )
        mWakeLock?.acquire()
    }


    private fun releaseWakeLock() {
        mWakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
}