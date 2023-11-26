package com.webitel.mobile_sdk.data.grps

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc.MethodDescriptor
import webitel.portal.CustomerGrpc


internal class GrpcInterceptor(
    private var deviceId: String,
    private var clientToken: String
): ClientInterceptor {

    private val DEVICE_ID_KEY = Metadata.Key.of("x-portal-device", ASCII_STRING_MARSHALLER)
    private val CLIENT_TOKEN_KEY = Metadata.Key.of("x-portal-client", ASCII_STRING_MARSHALLER)
    private val ACCEESS_TOKEN_KEY = Metadata.Key.of("x-portal-access", ASCII_STRING_MARSHALLER)
    private var accessToken: String = ""


    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun sendMessage(message: ReqT) {
                super.sendMessage(message)
            }

            override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                setupHeaders(headers, method?.bareMethodName ?: "undefined bareMethodName")

                super.start(object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onMessage(message: RespT) {
                        super.onMessage(message)
                    }
                }, headers)
            }
        }
    }


    fun setAccessToken(value: String) {
        accessToken = value
    }


    fun getAccessToken(): String {
        return accessToken
    }


    private fun setupHeaders(headers: Metadata?, bareMethodName: String) {
        if(deviceId.isNotEmpty()) headers?.put(DEVICE_ID_KEY, deviceId)
        else headers?.removeAll(DEVICE_ID_KEY)

        if(bareMethodName == CustomerGrpc.getTokenMethod().bareMethodName) {
            headers?.put(CLIENT_TOKEN_KEY, clientToken)
            headers?.removeAll(ACCEESS_TOKEN_KEY)

        } else {
            if(accessToken.isNotEmpty()) {
                headers?.put(ACCEESS_TOKEN_KEY, accessToken)
                headers?.removeAll(CLIENT_TOKEN_KEY)

            } else {
                headers?.put(CLIENT_TOKEN_KEY, clientToken)
                headers?.removeAll(ACCEESS_TOKEN_KEY)
            }
        }
    }
}