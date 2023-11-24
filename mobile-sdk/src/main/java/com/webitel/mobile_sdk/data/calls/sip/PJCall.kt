package com.webitel.mobile_sdk.data.calls.sip

import android.util.Log
import com.webitel.mobile_sdk.data.calls.sip.CallSettings
import com.webitel.mobile_sdk.data.calls.sip.PJAccount
import org.pjsip.pjsua2.AudDevManager
import org.pjsip.pjsua2.AudioMedia
import org.pjsip.pjsua2.Call
import org.pjsip.pjsua2.CallInfo
import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.OnCallMediaStateParam
import org.pjsip.pjsua2.OnCallStateParam
import org.pjsip.pjsua2.pjmedia_type
import org.pjsip.pjsua2.pjsip_inv_state
import org.pjsip.pjsua2.pjsip_status_code
import org.pjsip.pjsua2.pjsua_call_flag
import org.pjsip.pjsua2.pjsua_call_media_status

internal class PJCall: Call {
    var uuidString: String = ""
    private var event: SipCallCallbacks
    constructor(acc: PJAccount, callID: Int, endpoint: PJEndpoint, event: SipCallCallbacks) : super(acc, callID) {
        this.account = acc
        this.endpoint = endpoint
        this.event = event
    }

    constructor(acc: PJAccount, endpoint: PJEndpoint, event: SipCallCallbacks): super(acc) {
        this.account = acc
        this.endpoint = endpoint
        this.event = event
    }

    private var account: PJAccount
    private var endpoint: PJEndpoint

    var isOnMute: Boolean = false


    override fun onCallState(prm: OnCallStateParam?) {
        super.onCallState(prm)
        Log.e("onCallState3333333", info.state.toString())
        when(info.state) {
            pjsip_inv_state.PJSIP_INV_STATE_NULL -> {}
            pjsip_inv_state.PJSIP_INV_STATE_CALLING -> {}
            pjsip_inv_state.PJSIP_INV_STATE_INCOMING -> {}
            pjsip_inv_state.PJSIP_INV_STATE_EARLY -> {}
            pjsip_inv_state.PJSIP_INV_STATE_CONNECTING -> {}
            pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED -> {
                //account.onAnswerCall(id, uuidString)
                event.onAnswerCallPJSIP(id, uuidString)
            }
            pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED -> {
                event.onDisconnectPJSIP(id, uuidString)
                //account.onDisconnectCall(id, uuidString)
            }
        }
    }


    override fun onCallMediaState(prm: OnCallMediaStateParam?) {
        val ci = try {
            info
        } catch (e: java.lang.Exception) {
            //callbacks.onCallError(e)
            return
        }

        val cmiv = ci.media

        for (i in cmiv.indices) {
            val cmi = cmiv[i]
            if (cmi.type == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                cmi.status ==
                pjsua_call_media_status.PJSUA_CALL_MEDIA_LOCAL_HOLD
            ) {
                //set ur call status  as hold on your TextView
                event.onHoldCallPJSIP(id, uuidString)
                //account.onHoldCall(id, uuidString)
            } else if (cmi.type == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                cmi.status ==
                pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
            ) {
                event.onActiveCallPJSIP(id, uuidString)
                //account.onActiveCall(id, uuidString)
                //set ur call status  as connected on your TextView
            }
        }
        for (i in cmiv.indices) {
            val cmi = cmiv[i]
            if (cmi.type == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                (cmi.status == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
                        || cmi.status == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)
            ) {

                // unfortunately, on Java too, the returned Media cannot be downcasted to AudioMedia
                val m = getMedia(i.toLong())
                val am = AudioMedia.typecastFromMedia(m)

                // connect ports
                try {
                    val audDevManager: AudDevManager = endpoint.audDevManager()
                    audDevManager.captureDevMedia.startTransmit(am)
                    am.startTransmit(audDevManager.playbackDevMedia)
                } catch (e: java.lang.Exception) {
                    // callbacks.onCallError(e)
                }
            }
        }

        if (isOnMute) {
            setMute(true)
        }
    }


    fun toggleMute() {
        setMute(!isOnMute)
    }


    fun setMute(mute: Boolean) {
        // return immediately if we are not changing the current state
//        if (isOnMute && mute || !isOnMute && !mute) return

        val info: CallInfo = info

        for (i in info.media.indices) {

            val media = getMedia(i.toLong())
            val mediaInfo = info.media[i]

            if (mediaInfo.type == pjmedia_type.PJMEDIA_TYPE_AUDIO && media != null &&
                mediaInfo.status == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
            ) {

                val audioMedia = AudioMedia.typecastFromMedia(media)

                // connect or disconnect the captured audio
                try {

                    val mgr = endpoint.audDevManager()

                    isOnMute = if (mute) {
                        mgr?.captureDevMedia?.stopTransmit(audioMedia)
                        true

                    }else {
                        mgr?.captureDevMedia?.startTransmit(audioMedia)
                        false
                    }

                } catch (exc: java.lang.Exception) {

                    Log.e(
                        "setMute",
                        "setMute: error while connecting audio media to sound device",
                        exc
                    )
                }
            }
        }
    }


    fun transferTo(number: String, name: String) {
        val suffix = when (account.transportUse) {
            CallSettings.TransportUse.TCP -> ";transport=tcp"
            CallSettings.TransportUse.TLS -> ";transport=tls"
            else -> ""
        }
        val uri = account.config.getSipUri(number, name, suffix)
        transferTo(uri)
    }


    fun transferTo(destination: String) {
        try {
            val param = CallOpParam(true)
            xfer(destination, param)

        }catch(exc: Exception) {
            Log.e(
                "transferTo Err",
                exc.stackTraceToString()
            )
        }
    }


    fun setHold() {
        Log.e("setHold", isActive.toString())
        if(info.state != pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
            return
        }

        val param = CallOpParam(true)
        setHold(param)
    }


    fun setUnHold() {
        Log.e("setHold2", isActive.toString())
        if(info.state != pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED){
            return
        }

        val param = CallOpParam(true)
        param.opt.audioCount = pjsua_call_flag.PJSUA_CALL_UNHOLD.toLong()
        param.opt.flag = pjsua_call_flag.PJSUA_CALL_UNHOLD.toLong()

        reinvite(param)
    }


    fun hangup(code: Int) {
        val param = CallOpParam(true)
        param.statusCode = code
        try {
            hangup(param)
        } catch (exc: Exception) {
            Log.e(
                "hangupCall:Error",
                "Failed to hangup call",
                exc
            )
        }
    }


    fun answer() {
        val param = CallOpParam(true)
        param.statusCode = pjsip_status_code.PJSIP_SC_OK
        param.opt.audioCount = 1
        param.opt.videoCount = 0

        try {
            answer(param)

        } catch (exc: Exception) {
            Log.e(
                "answerIncomingCall:Error",
                "Failed to answer incoming call",
                exc
            )
        }
    }
}