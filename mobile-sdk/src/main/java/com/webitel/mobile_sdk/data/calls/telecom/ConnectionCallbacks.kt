package com.webitel.mobile_sdk.data.calls.telecom

import android.net.Uri
import android.os.Bundle
import android.telecom.CallAudioState
import android.telecom.Connection

internal interface ConnectionCallbacks {
    fun onCallAudioStateChanged(state: CallAudioState?)
    fun onUsingAlternativeUi(isUsingAlternativeUi: Boolean)
    fun onTrackedByNonUiService(isTracked: Boolean)
    fun onStateChanged(state: Int)
    fun onPlayDtmfTone(c: Char)
    fun onStopDtmfTone()
    fun onDisconnect()
    fun onSeparate()
    fun onAddConferenceParticipants(participants: MutableList<Uri>)
    fun onAbort()
    fun onHold()
    fun onUnhold()
    fun onAnswer()
    fun onAnswer(videoState: Int)
    fun onDeflect(address: Uri?)
    fun onReject()
    fun onReject(rejectReason: Int)
    fun onReject(replyMessage: String?)
    fun onSilence()
    fun onPostDialContinue(proceed: Boolean)
    fun onPullExternalCall()
    fun onCallEvent(event: String?, extras: Bundle?)
    fun onHandoverComplete()
    fun onExtrasChanged(extras: Bundle?)
    fun onShowIncomingCallUi()
    fun onStartRtt(rttTextStream: Connection.RttTextStream)
    fun onStopRtt()
}