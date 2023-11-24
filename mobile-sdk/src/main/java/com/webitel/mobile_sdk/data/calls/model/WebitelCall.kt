package com.webitel.mobile_sdk.data.calls.model

import android.net.Uri
import android.os.Bundle
import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.DisconnectCause
import android.util.Log
import com.webitel.mobile_sdk.data.calls.CallsRepository
import com.webitel.mobile_sdk.data.calls.sip.PJCall
import com.webitel.mobile_sdk.data.calls.sip.SipCallCallbacks
import com.webitel.mobile_sdk.data.calls.sip.SipConfig
import com.webitel.mobile_sdk.data.calls.telecom.CallConnection
import com.webitel.mobile_sdk.data.calls.telecom.ConnectionCallbacks
import com.webitel.mobile_sdk.domain.Call
import com.webitel.mobile_sdk.domain.CallState
import com.webitel.mobile_sdk.domain.CallStateListener
import com.webitel.mobile_sdk.domain.Error
import org.pjsip.pjsua2.pjsip_status_code
import java.util.Timer
import java.util.UUID


internal class WebitelCall(
    val repository: CallsRepository,
    listener: CallStateListener,
    override val toNumber: String,
    override val toName: String,
    override val from: String = "",
    val config: SipConfig,
    override val id: String = UUID.randomUUID().toString(),
) : Call, ConnectionCallbacks, SipCallCallbacks {

    private var telecomCall: CallConnection? = null
    private var pjsipCall: PJCall? = null

    private val listeners = CallsListeners()
    private var _answeredAt: Long = 0L
    private var _dtmfHistory: String = ""
    private var _callState: ArrayList<CallState> = arrayListOf()

    var finishTimer: Timer? = null
    private var autoAnswerTimer: Timer? = null

    private var inProcessing = false
    private var isOnHold: Boolean = false
    var isInbound: Boolean = false
    private var needAutoAnswer: Boolean = false

    private val isAnswered: Boolean
        get() {
            return _answeredAt > 0
        }

    override val answeredAt: Long
        get() = _answeredAt

    override val dtmfHistory: String
        get() = _dtmfHistory

    override val state: List<CallState>
        get() = _callState


    init {
        listeners.addListener(listener)
    }


    override fun toggleLoudspeaker() {
        repository.make {
            if (isValidState()) {
                CallsRepository.isSpeakerOn = !CallsRepository.isSpeakerOn
                setAudioRoute()
                if (CallsRepository.isSpeakerOn) {
                    addState(CallState.LOUDSPEAKER)
                } else {
                    deleteState(CallState.LOUDSPEAKER)
                }
            }
        }
    }


    override fun toggleHold() {
        repository.make {
            if (!inProcessing && isValidState()) {
                inProcessing = true
                if (isOnHold) {
                    repository.holdOtherCalls(id)
                    telecomCall?.onUnhold()
                }
                else telecomCall?.onHold()
            }
        }
    }


    override fun sendDigits(value: String) {
        if (isValidState() && pjsipCall != null) {
            repository.make {
                repository.checkPjsipThread()
                pjsipCall?.dialDtmf(value)
                addDtmfToHistory(value)
            }
        }
    }


    override fun addListener(listener: CallStateListener) {
        listeners.addListener(listener)
    }


    override fun removeListener(listener: CallStateListener) {
        listeners.removeListener(listener)
    }


    override fun removeAllListeners() {
        listeners.removeAllListeners()
    }


    override fun toggleMute() {
        if (isValidState()) {
            repository.make {
                repository.checkPjsipThread()
                pjsipCall?.toggleMute()

                if (pjsipCall?.isOnMute == true) {
                    addState(CallState.MUTE)
                } else {
                    deleteState(CallState.MUTE)
                }
            }
        }
    }


    override fun disconnect() {
        repository.make {
            telecomCall?.onReject()
        }
    }


    fun setSipConnection(c: PJCall) {
        pjsipCall = c
        addState(CallState.DIALING)
    }


    fun setRegisterAccountState() {
        addState(CallState.REGISTERING_SIP_ACCOUNT)
    }


    fun setHold() {
        if (!isOnHold) {
            telecomCall?.setOnHold()
            telecomCall?.onHold()
        }
    }


    fun setTelecomCall(call: CallConnection) {
        telecomCall = call
    }


    fun makeSipCall() {
        repository.makeSipCallFor(this)
    }


    fun getConnection(): CallConnection? {
        return telecomCall
    }


    fun onCreateCallFailed(e: Error) {
        listeners.onCreateCallFailed(e)
    }


    fun destroyCall() {
        if (_callState.contains(CallState.DISCONNECTED)) return
        setDisconnectState()
        setNormalAudioRouteIfNoOtherCalls()

        repository.make {
            repository.checkPjsipThread()
            pjsipCall?.hangup(getHangupCode(false))
            pjsipCall?.delete()
            pjsipCall = null
        }

        finishTimer?.cancel()
        finishTimer = null

        autoAnswerTimer?.cancel()
        autoAnswerTimer = null

        telecomCall?.setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        telecomCall?.onDisconnect()
        telecomCall?.destroy()
        telecomCall = null

        repository.deleteCall(id)
    }


    private fun isValidState(): Boolean {
        return _callState.contains(CallState.ACTIVE)
    }


    private fun addState(value: CallState) {
        if (!_callState.contains(value)) {
            val oldState = _callState.toList()
            _callState.add(value)
            listeners.onCallStateChanged(this, oldState)
        }
    }


    private fun deleteState(value: CallState) {
        if (_callState.contains(value)) {
            val oldState = _callState.toList()
            _callState.remove(value)
            listeners.onCallStateChanged(this, oldState)
        }
    }


    private fun setDisconnectState() {
        _callState.clear()
        addState(CallState.DISCONNECTED)
        listeners.removeAllListeners()
    }


    private fun setActiveState() {
        _callState.removeAll(
            listOf(
                CallState.REGISTERING_SIP_ACCOUNT,
                CallState.DIALING,
                CallState.RINGING
            )
        )
        addState(CallState.ACTIVE)
    }


    private fun addDtmfToHistory(character: String) {
        _dtmfHistory += character
    }


    private fun setAudioRoute() {
        val route = repository.getActiveAudioRoute()
        telecomCall?.setAudioRoute(route)
        if (CallsRepository.isSpeakerOn) {
            addState(CallState.LOUDSPEAKER)
        } else {
            deleteState(CallState.LOUDSPEAKER)
        }
    }


    private fun isOnMute(): Boolean {
        return pjsipCall?.isOnMute ?: false
    }


    private fun setNormalAudioRouteIfNoOtherCalls() {
        if (isOtherCalls(id)) {
            CallsRepository.isSpeakerOn = false
            setAudioRoute()
        }
    }


    private fun getHangupCode(busyEverywhere: Boolean): Int {
        return when {
            isAnswered -> pjsip_status_code.PJSIP_SC_OK
            isInbound -> {
                if (busyEverywhere) {
                    pjsip_status_code.PJSIP_SC_BUSY_EVERYWHERE
                } else {
                    pjsip_status_code.PJSIP_SC_BUSY_HERE
                }
            }
            else -> pjsip_status_code.PJSIP_SC_REQUEST_TERMINATED
        }
    }


    private fun isOtherCalls(id: String): Boolean {
        var isOtherCalls = false
        CallsRepository.activeCalls.forEach {
            if (it.key != id) isOtherCalls = true
        }
        return isOtherCalls
    }


    // Telecom callbacks

    override fun onCallAudioStateChanged(state: CallAudioState?) {}

    override fun onUsingAlternativeUi(isUsingAlternativeUi: Boolean) {}

    override fun onTrackedByNonUiService(isTracked: Boolean) {}

    override fun onStateChanged(state: Int) {}

    override fun onPlayDtmfTone(c: Char) {}

    override fun onStopDtmfTone() {}


    override fun onDisconnect() {
        repository.deleteCall(id)
        destroyCall()
    }


    override fun onSeparate() {}

    override fun onAddConferenceParticipants(participants: MutableList<Uri>) {}

    override fun onAbort() {}


    override fun onHold() {
        repository.make {
            repository.checkPjsipThread()
            pjsipCall?.setHold()
        }
    }


    override fun onUnhold() {
        repository.make {
            repository.checkPjsipThread()
            pjsipCall?.setUnHold()
        }
    }


    override fun onAnswer() {
        autoAnswerTimer?.cancel()
        autoAnswerTimer = null
        repository.make {
            repository.checkPjsipThread()
            repository.holdOtherCalls(id)

            if (pjsipCall != null) {
                pjsipCall?.answer()

            } else {
                needAutoAnswer = true
                _answeredAt = System.currentTimeMillis()
            }

            setActiveState()
        }
    }


    override fun onAnswer(videoState: Int) {}

    override fun onDeflect(address: Uri?) {}

    override fun onReject() {
        destroyCall()
    }

    override fun onReject(rejectReason: Int) {
        destroyCall()
    }

    override fun onReject(replyMessage: String?) {}

    override fun onSilence() {}

    override fun onPostDialContinue(proceed: Boolean) {}

    override fun onPullExternalCall() {}

    override fun onCallEvent(event: String?, extras: Bundle?) {}

    override fun onHandoverComplete() {}

    override fun onExtrasChanged(extras: Bundle?) {}

    override fun onShowIncomingCallUi() {}

    override fun onStartRtt(rttTextStream: Connection.RttTextStream) {}

    override fun onStopRtt() {}


    // pjsip callbacks

    override fun onAnswerCallPJSIP(callId: Int, webitelId: String) {
        telecomCall?.setActive()

        if (!isAnswered) {
            _answeredAt = System.currentTimeMillis()
        }

        setAudioRoute()
        setActiveState()
    }


    override fun onDisconnectPJSIP(callId: Int, webitelId: String) {
        repository.make {
            repository.checkPjsipThread()
            repository.deleteCall(id)
            setNormalAudioRouteIfNoOtherCalls()
            setDisconnectState()
            pjsipCall?.delete()
            pjsipCall = null

            telecomCall?.setDisconnected(
                DisconnectCause(DisconnectCause.LOCAL)
            )
            telecomCall?.destroy()
            autoAnswerTimer?.cancel()
            autoAnswerTimer = null
        }
    }


    override fun onActiveCallPJSIP(callId: Int, webitelId: String) {
        inProcessing = false
        isOnHold = false
        if (!isOnMute()) {
            telecomCall?.setActive()
            setAudioRoute()
        }

        deleteState(CallState.HOLD)
    }


    override fun onHoldCallPJSIP(callId: Int, webitelId: String) {
        inProcessing = false
        isOnHold = true
        telecomCall?.setOnHold()
        addState(CallState.HOLD)
    }


    override fun onCallErrorPJSIP(e: Exception?) {
        Log.e("onCallErrorPJSIP", e?.message.toString())
    }
}


