package com.webitel.mobile_sdk.data.calls.sip

import android.util.Log
import org.pjsip.pjsua2.AccountConfig
import org.pjsip.pjsua2.AuthCredInfo
import org.pjsip.pjsua2.CallOpParam
import org.pjsip.pjsua2.EpConfig
import org.pjsip.pjsua2.TransportConfig
import org.pjsip.pjsua2.pj_log_decoration
import org.pjsip.pjsua2.pj_qos_type
import org.pjsip.pjsua2.pjsip_status_code
import org.pjsip.pjsua2.pjsip_transport_type_e
import com.webitel.mobile_sdk.data.calls.sip.CallSettings.TransportUse
import kotlin.Exception

internal class SipManager {

    companion object {
        private var endpoint: PJEndpoint? = null
        private var account: PJAccount? = null
        private var isDestroying: Boolean = false
    }


    constructor( callback: SipCallbacks) {
        this.callback = callback
        loadNativeLibraries()
        logWriter  = SipLogWriter()
    }
    private var callback: SipCallbacks
    private var logWriter: SipLogWriter
   // private var credentials: WebitelCredentials? = null


    fun createAccount(settings: CallSettings, config: SipConfig) {
        try {
            endpoint = PJEndpoint()
            endpoint?.libCreate()

            val epConfig = getEndpointConfig(
                proxy = config.getProxy(),
                transport = settings.transport
            )


            endpoint?.libInit(epConfig)

            setTransportConfig(settings.transport)

            setCodecPriority(settings.codecPriority)

            /* Start the library */
            endpoint?.libStart()

            val accountConfig = getAccountConfig(
                config, settings
            )

            /* Create the account */
            account = PJAccount(callback, settings.transport, config)
            //account!!.setDefault()
            account?.create(accountConfig, true)
            endpoint?.libRegisterThread(Thread.currentThread().name)

            endpoint?.audDevManager()?.setOutputVolume(100, true)
            endpoint?.audDevManager()?.setInputVolume(100, true)

        } catch (e: Exception) {
            Log.e(
                "createAccount",
                e.stackTraceToString()
            )
        }
    }


    fun isAccountCreated(): Boolean {
        return account != null
    }


    fun isAccountRegistered(): Boolean {
        return account?.isRegistered() ?: false
    }


    fun confirmIncomingCall(callId: String) {
//        credentials?.let {
//            val executors = Executors.newSingleThreadExecutor()
//            try {
//                val api = NetworkModule().createCallsApi(it.getUrl(), it.token)
//                executors.execute {
//                    api.confirmPush(callId).execute()
//                }
//            } catch (e: Exception) {
//                Log.e("confirmIncomingCall", e.message.toString())
//            } finally {
//                executors.shutdown()
//            }
//        }
    }


    fun newOutgoingCall(callback: SipCallCallbacks, number: String, name: String = ""): PJCall? {
        checkThread()
        return makeSipCall(callback, number, name)
    }


    fun newIncomingCall(callback: SipCallCallbacks, pjCallId: Int): PJCall? {
        val a = account ?: return null
        val e = endpoint ?: return null

        checkThread()

        val pjCall = PJCall(a, pjCallId, e, callback)
        val prm = CallOpParam()
        prm.statusCode = pjsip_status_code.PJSIP_SC_RINGING
        pjCall.answer(prm)

        //pjCall.uuidString = callId

        return pjCall
    }


    fun createTransferUri(number: String, name: String = ""): String {
        val a = account ?: return ""
        val suffix = getTransportSuffixForUri(a.transportUse)
        return a.config.getSipUri(number, name, suffix)
    }


    private fun makeSipCall(callback: SipCallCallbacks, number: String, name: String): PJCall? {
        val a = account ?: return null
        val e = endpoint ?: return null

        val pjCall = PJCall(a, e, callback)

        val callOpParam = CallOpParam()
        callOpParam.opt.videoCount = 0
        callOpParam.opt.audioCount = 1

        val suffix = getTransportSuffixForUri(a.transportUse)
        val remoteUri = a.config.getSipUri(number, name, suffix)

        pjCall.makeCall(remoteUri, callOpParam)

        return pjCall
    }


    private fun getAccountConfig(config: SipConfig, settings: CallSettings): AccountConfig {
        val idUri = "sip:" + config.extension +
                "@" + config.domain

        val accountConfig = AccountConfig()
        accountConfig.priority = 100
        accountConfig.idUri = idUri
        accountConfig.regConfig.registerOnAdd = false

        //accountConfig.sipConfig.proxies.clear()
        //accountConfig.sipConfig.proxies.add(sipConfig!!.getProxy())

        accountConfig.regConfig.proxyUse = 1
        accountConfig.regConfig.registrarUri = config.getProxy()

        accountConfig.natConfig.mediaStunUse = settings.natMediaStunUse.value
        accountConfig.natConfig.sipStunUse = settings.natSipStunUse.value
        accountConfig.natConfig.iceEnabled = settings.natIceEnabled
        accountConfig.natConfig.sdpNatRewriteUse = if(settings.natSdpNatRewriteUse) 1 else 0
        accountConfig.natConfig.contactRewriteUse = if(settings.natContactRewriteUse) 1 else 0
        accountConfig.natConfig.viaRewriteUse = if(settings.natViaRewriteUse) 1 else 0
        //accountConfig.natConfig.contactRewriteMethod =
        accountConfig.mediaConfig.transportConfig.qosType = pj_qos_type.PJ_QOS_TYPE_VOICE
        accountConfig.mediaConfig.srtpUse = settings.srtpUse.value
        accountConfig.mediaConfig.srtpSecureSignaling = 0

        val cred = AuthCredInfo(
            "digest",
            "*",
            config.auth,
            0,
            config.password
        )

        accountConfig.sipConfig.authCreds.add(cred)

        return accountConfig
    }


    private fun getEndpointConfig(proxy: String, transport: TransportUse): EpConfig {
        val epConfig = EpConfig()
        epConfig.uaConfig.maxCalls = 7
        epConfig.uaConfig.userAgent = "webitel portal"
        epConfig.medConfig.hasIoqueue = true
        epConfig.medConfig.clockRate = 16000
        epConfig.medConfig.quality = 10
        epConfig.medConfig.ecOptions = 1
        epConfig.medConfig.ecTailLen = 200
        epConfig.medConfig.threadCnt = 2

        epConfig.uaConfig.outboundProxies.clear()
        epConfig.uaConfig.outboundProxies.add(
            proxy + getTransportSuffix(transport)
        )

        setLogConfig(epConfig)

        return epConfig
    }


//    fun setCredentials(credential: WebitelCredentials) {
//        this.credentials = credential
//    }


    private fun setCodecPriority(codecPriority: MutableMap<String, Short>) {
        try {
            codecPriority.forEach { entry ->
                endpoint?.codecSetPriority(entry.key, entry.value)
            }
            endpoint?.videoCodecSetPriority("H264/97", 0)
//            val vidCodecParam: VidCodecParam = endpoint!!.getVideoCodecParam("H264/97")
//            val codecFmtpVector = vidCodecParam.decFmtp
//            val mediaFormatVideo = vidCodecParam.encFmt
//            mediaFormatVideo.width = 640
//            mediaFormatVideo.height = 360
//            vidCodecParam.encFmt = mediaFormatVideo
//
//            for (i in codecFmtpVector.indices) {
//                if ("42e01f".equals(codecFmtpVector[i].name)) {
//                    codecFmtpVector[i].setVal("42e01f")
//                    break
//                }
//            }
//            vidCodecParam.decFmtp = codecFmtpVector
//            endpoint!!.setVideoCodecParam("H264/97", vidCodecParam)
            //printCodecs()
        }catch (e: Exception){
            Log.e("codecSetPriority", e.message.toString())
        }
    }


    private fun setTransportConfig(transport: TransportUse) {
        when (transport) {
            TransportUse.UDP -> createTransportUDP()
            TransportUse.TCP -> createTransportTCP()
            TransportUse.TLS -> createTransportTLS()
            TransportUse.TCP_UDP -> {
                createTransportUDP()
                createTransportTCP()
            }
        }
    }


    private fun getTransportSuffix(transport: TransportUse): String {
        return when (transport) {
            TransportUse.UDP -> ";transport=udp"
            TransportUse.TCP -> ";transport=tcp"
            TransportUse.TLS -> ";transport=tls"
            TransportUse.TCP_UDP -> ";transport=tcp"
        }
    }

    
    private fun getTransportSuffixForUri(transport: TransportUse): String {
        return when (transport) {
            TransportUse.TCP -> ";transport=tcp"
            TransportUse.TLS -> ";transport=tls"
            else -> ""
        }
    }


    private fun createTransportUDP() {
        val udpTransport = TransportConfig()
        udpTransport.qosType = pj_qos_type.PJ_QOS_TYPE_VOICE
        endpoint?.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, udpTransport)
    }


    private fun createTransportTCP() {
        val tcpTransport = TransportConfig()
        tcpTransport.qosType = pj_qos_type.PJ_QOS_TYPE_VOICE
        endpoint?.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TCP, tcpTransport)
    }


    private fun createTransportTLS() {
        val tlsTransport = TransportConfig()
        tlsTransport.qosType = pj_qos_type.PJ_QOS_TYPE_VOICE
        endpoint?.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TLS, tlsTransport)
    }


    private fun setLogConfig(epConfig: EpConfig) {
        logWriter = SipLogWriter()
        epConfig.logConfig.level = 3
        epConfig.logConfig.consoleLevel = 3

        val logCfg = epConfig.logConfig
        logCfg.writer = logWriter
        logCfg.decor =
            logCfg.decor and (
                    pj_log_decoration.PJ_LOG_HAS_CR.toLong()
                            or pj_log_decoration.PJ_LOG_HAS_NEWLINE.toLong()
                    )
                .inv()
    }


    @Synchronized
    internal fun checkThread() {
        try {
            if (endpoint != null && endpoint?.libIsThreadRegistered() == false) {
                endpoint?.libRegisterThread(
                    Thread.currentThread().name
                )
            }
        } catch (e: Exception) {
            Log.e(
                "libRegisterThread Err:",
                "Threading: libRegisterThread failed: " + e.message
            )
        }
    }

    @Synchronized
    fun stopStack() {
        if (isDestroying) return
        isDestroying = true
        checkThread()
        endpoint?.hangupAllCalls()
        endpoint?.libDestroy()
        endpoint?.delete()
        account?.shutdown()
        account?.delete()

        endpoint = null
        account = null
        isDestroying = false
    }


    fun isDestroying(): Boolean {
        return isDestroying
    }


    fun quickDestroy() {
        endpoint = null
        account = null
        isDestroying = false
    }


    private fun loadNativeLibraries(){
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
}