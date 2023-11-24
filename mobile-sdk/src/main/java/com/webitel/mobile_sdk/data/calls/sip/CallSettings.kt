package com.webitel.mobile_sdk.data.calls.sip

class CallSettings {
    constructor()

    constructor (
        transport: Int,
        natMediaStunUse: Int,
        natSipStunUse: Int,
        natIceEnabled: Boolean,
        natSdpNatRewriteUse: Boolean,
        natContactRewriteUse: Boolean,
        natViaRewriteUse: Boolean,
        busyEverywhereUse: Boolean,
        srtpSecureSignaling: Int,
        srtpUse: Int
    ) {
        this.transport = TransportUse.values()[transport]
        this.natMediaStunUse = MediaStunUse.values()[natMediaStunUse]
        this.natSipStunUse = MediaStunUse.values()[natSipStunUse]
        this.natIceEnabled = natIceEnabled
        this.natSdpNatRewriteUse = natSdpNatRewriteUse
        this.natContactRewriteUse = natContactRewriteUse
        this.natViaRewriteUse = natViaRewriteUse
        this.srtpSecureSignaling = srtpSecureSignaling
        this.srtpUse = SrtpUse.values()[srtpUse]
        this.busyEverywhereUse = busyEverywhereUse
    }


    override fun toString(): String {
        return "transport: ${transport}; natMediaStunUse: $natMediaStunUse; natSipStunUse: $natSipStunUse; " +
                "natIceEnabled: $natIceEnabled; natSdpNatRewriteUse: $natSdpNatRewriteUse; natContactRewriteUse: $natContactRewriteUse" +
                "; natViaRewriteUse: $natViaRewriteUse; srtpSecureSignaling: $srtpSecureSignaling; srtpUse: $srtpUse"
    }


    val WAITING_TO_CALL_MILLIS: Long = 5000

    // Transport config
    var transport: TransportUse = TransportUse.UDP

    // Nat config
    var natMediaStunUse: MediaStunUse = MediaStunUse.PJSUA_STUN_USE_DEFAULT
    var natSipStunUse: MediaStunUse = MediaStunUse.PJSUA_STUN_USE_DEFAULT
    var natIceEnabled: Boolean = false
    var natSdpNatRewriteUse: Boolean = false
    var natContactRewriteUse: Boolean = true
    var natViaRewriteUse: Boolean = true

    // Media config
    var srtpSecureSignaling: Int = 0
    var srtpUse: SrtpUse = SrtpUse.PJMEDIA_SRTP_DISABLED

    // Others
    var busyEverywhereUse: Boolean = false

    // Codec priority
    val codecPriority = mutableMapOf<String, Short>(
        "opus/48000/2" to 255,
        "G722/16000/1" to 200,
        "PCMA/8000/1" to 155,
        "PCMU/8000/1" to 100,
        "GSM/8000/1" to 0,
        "iLBC/8000/1" to 0,
        "AMR-WB/16000/1" to 0,
        "AMR/8000/1" to 0,
        "G729/8000/1" to 0,
        "G7221/32000/1" to 0,
        "G7221/16000/1" to 0,
        "speex/32000/1" to 0,
        "speex/16000/1" to 0,
        "speex/8000/1" to 0
    )


    enum class SrtpUse(val value: Int) {
        PJMEDIA_SRTP_DISABLED(0),
        PJMEDIA_SRTP_OPTIONAL(1),
        PJMEDIA_SRTP_MANDATORY(2)
    }


    enum class TransportUse(val value: Int) {
        UDP(0),
        TCP(1),
        TCP_UDP(2),
        TLS(3)
    }


    enum class MediaStunUse(val value: Int) {
        PJSUA_STUN_USE_DEFAULT(0),
        PJSUA_STUN_USE_DISABLED(1),
        PJSUA_STUN_RETRY_ON_FAILURE(2)
    }
}