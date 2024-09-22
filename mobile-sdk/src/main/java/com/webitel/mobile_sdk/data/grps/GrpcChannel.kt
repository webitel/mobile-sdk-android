package com.webitel.mobile_sdk.data.grps

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit


internal class GrpcChannel(
    private val config: ChannelConfig
) {
    private val interceptor: GrpcInterceptor
    val channel: ManagedChannel


    init {
        interceptor = GrpcInterceptor(
            deviceId = config.deviceId,
            clientToken = config.clientToken
        )
        channel = ManagedChannelBuilder
            .forAddress(
                config.host, // "dev.webitel.com",
                config.port // 443
            )
            .keepAliveWithoutCalls(false)
            .keepAliveTime(config.keepAliveTime, TimeUnit.SECONDS)
            .keepAliveTimeout(config.keepAliveTimeout, TimeUnit.SECONDS)
            .userAgent(config.agent)
            .intercept(interceptor)
            .build()
    }


     fun isActive(): Boolean {
        return !channel.isShutdown && !channel.isTerminated
    }


    fun equal(c: ChannelConfig): Boolean {
        return config == c
    }


    fun setAccessToken(value: String) {
        interceptor.setAccessToken(value)
    }


    fun getAccessToken(): String {
        return interceptor.getAccessToken()
    }


    fun getDeviceId(): String {
        return config.deviceId
    }


    fun setStreamListener(listener: StreamListener?) {
        interceptor.setStreamListener(listener)
    }
}