package com.webitel.mobile_sdk.data.wss

import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import okhttp3.Interceptor
import okhttp3.Response


internal class HeaderInterceptor(
    private val headerProvider: HeaderProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()

        headerProvider.commonHeaders().forEach { (key, value) ->
            logger.debug("HeaderInterceptor", "$key: $value")
            builder.header(key, value)
        }

        return chain.proceed(builder.build())
    }
}