package com.webitel.mobile_sdk.data.calls

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.webitel.mobile_sdk.data.calls.model.WebitelCall
import com.webitel.mobile_sdk.data.calls.sip.SipConfig
import com.webitel.mobile_sdk.data.grps.GrpcListener
import com.webitel.mobile_sdk.data.LibraryModule
import com.webitel.mobile_sdk.domain.Call
import com.webitel.mobile_sdk.domain.CallStateListener
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.VoiceClient
import webitel.portal.Connect
import java.util.UUID


internal class WebitelVoice(
    private val api: VoiceApi
    ) : VoiceClient, GrpcListener {

    val repo = CallsRepository.instance
    override val activeCall: Call?
        get() = getCall()


    override fun makeCall(listener: CallStateListener) {
        if(!hasRecordAudioPermission(LibraryModule.application)) {
            listener.onCreateCallFailed(
                Error(
                    message = "does not have android.permission.RECORD_AUDIO",
                    code = Code.PERMISSION_DENIED
                )
            )
            return
        }

        if (CallsRepository.activeCalls.isNotEmpty()) {
            val call = getCall()
            call?.let {
                call.addListener(listener)
                listener.onCreateCall(it)
            }
            return
        }

        api.getSipConfig(object: CallbackListener<SipConfig> {
            override fun onSuccess(t: SipConfig) {
                val call = WebitelCall(
                    repository = repo,
                    listener = listener,
                    toNumber = "service",
                    toName = "service",
                    from = t.extension ?: "",
                    config = t,
                    id = UUID.randomUUID().toString()
                )
                listener.onCreateCall(call)
                repo.makeCall(call)
            }

            override fun onError(e: Error) {
                listener.onCreateCallFailed(e)
            }
        })
    }


    override fun onResponse(response: Connect.Response) {}

    override fun onConnectionError(e: Error) {}

    override fun onConnectionReady() {}


    private fun getCall(): Call? {
        return CallsRepository
            .activeCalls
            .values
            .firstOrNull()
    }


    private fun hasRecordAudioPermission(context: Context): Boolean {
        val recordAudio =
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        return recordAudio == PackageManager.PERMISSION_GRANTED
    }
}