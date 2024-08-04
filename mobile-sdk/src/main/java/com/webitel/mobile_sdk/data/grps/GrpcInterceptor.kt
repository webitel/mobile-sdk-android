package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.data.portal.WebitelPortalClient
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc.MethodDescriptor
import io.grpc.Status


internal class GrpcInterceptor(
    private var deviceId: String,
    private var clientToken: String
): ClientInterceptor {

    private val DEVICE_ID_KEY = Metadata.Key.of("x-portal-device", ASCII_STRING_MARSHALLER)
    private val CLIENT_TOKEN_KEY = Metadata.Key.of("x-portal-client", ASCII_STRING_MARSHALLER)
    private val ACCEESS_TOKEN_KEY = Metadata.Key.of("x-portal-access", ASCII_STRING_MARSHALLER)
    private var accessToken: String = ""

    private var listener: StreamListener? = null

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun sendMessage(message: ReqT) {
                super.sendMessage(message)
                WebitelPortalClient.logger.debug(
                    "intercept",
                    "Method: ${method?.bareMethodName ?: "undefined bareMethodName"}, Message: ${message.toString()}"
                )
            }



            override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                setupHeaders(headers, method?.bareMethodName ?: "undefined bareMethodName")
                listener?.onStart(method?.bareMethodName.toString())

                WebitelPortalClient.logger.debug(
                    "connect.start",
                    "${method?.bareMethodName.toString()} - ${headers.toString()}"
                )

                super.start(object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onMessage(message: RespT) {
                        super.onMessage(message)
                    }

                    override fun onReady() {
                        listener?.onReady(method?.bareMethodName.toString())
                        super.onReady()
                    }

                    override fun onClose(status: Status?, trailers: Metadata?) {
                        super.onClose(status, trailers)
                        listener?.onClose(method?.bareMethodName.toString(), status, trailers)
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


    fun setStreamListener(l: StreamListener?) {
        listener = l
    }


    private fun setupHeaders(headers: Metadata?, bareMethodName: String) {
        if(deviceId.isNotEmpty()) headers?.put(DEVICE_ID_KEY, deviceId)
        else headers?.removeAll(DEVICE_ID_KEY)

        if(clientToken.isNotEmpty()) headers?.put(CLIENT_TOKEN_KEY, clientToken)
        else headers?.removeAll(CLIENT_TOKEN_KEY)

        if(accessToken.isNotEmpty()) headers?.put(ACCEESS_TOKEN_KEY, accessToken)
        else headers?.removeAll(ACCEESS_TOKEN_KEY)
    }
}