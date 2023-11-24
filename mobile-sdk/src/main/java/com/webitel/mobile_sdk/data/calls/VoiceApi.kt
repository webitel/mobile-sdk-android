package com.webitel.mobile_sdk.data.calls

import com.webitel.mobile_sdk.data.calls.sip.SipConfig
import com.webitel.mobile_sdk.domain.CallbackListener


internal interface VoiceApi {
    fun getSipConfig(callback: CallbackListener<SipConfig>)
}