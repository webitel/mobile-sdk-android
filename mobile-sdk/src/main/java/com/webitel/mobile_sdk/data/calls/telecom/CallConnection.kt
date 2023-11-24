package com.webitel.mobile_sdk.data.calls.telecom

import android.net.Uri
import android.os.Bundle
import android.telecom.CallAudioState
import android.telecom.Connection


internal class CallConnection(private val callback: ConnectionCallbacks): Connection() {
    var displayName: String = ""
    var number: String = ""
    var remoteUri: String = ""
    var queueName: String = ""
    var isOnHold: Boolean = false
    var answeredAt: Long = 0
    var isInbound: Boolean = false


    var historyDtmf: String = ""
        private set



    fun addDtmfToHistory(character: String) {
        historyDtmf += character
    }



    override fun onCallAudioStateChanged(state: CallAudioState?) {
        super.onCallAudioStateChanged(state)

        callback.onCallAudioStateChanged(state)
    }


    override fun onUsingAlternativeUi(isUsingAlternativeUi: Boolean) {
        super.onUsingAlternativeUi(isUsingAlternativeUi)
        callback.onUsingAlternativeUi(isUsingAlternativeUi)
    }


    override fun onTrackedByNonUiService(isTracked: Boolean) {
        super.onTrackedByNonUiService(isTracked)
        callback.onTrackedByNonUiService(isTracked)
    }


    override fun onStateChanged(state: Int) {
        super.onStateChanged(state)
        callback.onStateChanged(state)
    }


    override fun onPlayDtmfTone(c: Char) {
        super.onPlayDtmfTone(c)
        callback.onPlayDtmfTone(c)
    }


    override fun onStopDtmfTone() {
        super.onStopDtmfTone()
        callback.onStopDtmfTone()
    }


    override fun onDisconnect() {
        super.onDisconnect()
        callback.onDisconnect()
    }


    override fun onSeparate() {
        super.onSeparate()
        callback.onSeparate()
    }


    override fun onAddConferenceParticipants(participants: MutableList<Uri>) {
        super.onAddConferenceParticipants(participants)
        callback.onAddConferenceParticipants(participants)
    }


    override fun onAbort() {
        super.onAbort()
        callback.onAbort()
    }


    override fun onHold() {
        super.onHold()
        callback.onHold()
    }


    override fun onUnhold() {
        super.onUnhold()
        callback.onUnhold()
    }


    override fun onAnswer(videoState: Int) {
        super.onAnswer(videoState)
        callback.onAnswer(videoState)
    }


    override fun onAnswer() {
        super.onAnswer()
        callback.onAnswer()
    }


    override fun onDeflect(address: Uri?) {
        super.onDeflect(address)
        callback.onDeflect(address)
    }


    override fun onReject() {
        super.onReject()
        callback.onReject()
    }


    override fun onReject(rejectReason: Int) {
        super.onReject(rejectReason)
        callback.onReject(rejectReason)
    }


    override fun onReject(replyMessage: String?) {
        super.onReject(replyMessage)
        callback.onReject(replyMessage)
    }


    override fun onSilence() {
        super.onSilence()
        callback.onSilence()
    }


    override fun onPostDialContinue(proceed: Boolean) {
        super.onPostDialContinue(proceed)
        callback.onPostDialContinue(proceed)
    }


    override fun onPullExternalCall() {
        super.onPullExternalCall()
        callback.onPullExternalCall()
    }


    override fun onCallEvent(event: String?, extras: Bundle?) {
        super.onCallEvent(event, extras)
        callback.onCallEvent(event, extras)
    }


    override fun onHandoverComplete() {
        super.onHandoverComplete()
        callback.onHandoverComplete()
    }


    override fun onExtrasChanged(extras: Bundle?) {
        super.onExtrasChanged(extras)
        callback.onExtrasChanged(extras)
    }


    override fun onShowIncomingCallUi() {
        super.onShowIncomingCallUi()
        callback.onShowIncomingCallUi()
    }


    override fun onStartRtt(rttTextStream: RttTextStream) {
        super.onStartRtt(rttTextStream)
        callback.onStartRtt(rttTextStream)
    }


    override fun onStopRtt() {
        super.onStopRtt()
        callback.onStopRtt()
    }
}