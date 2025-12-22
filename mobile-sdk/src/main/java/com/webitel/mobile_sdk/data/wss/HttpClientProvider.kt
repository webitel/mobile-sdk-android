package com.webitel.mobile_sdk.data.wss

import okhttp3.OkHttpClient


internal interface HttpClientProvider {
    fun client(): OkHttpClient
}