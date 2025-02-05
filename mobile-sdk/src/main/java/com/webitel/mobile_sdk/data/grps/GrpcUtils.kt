package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import io.grpc.ManagedChannel
import java.util.concurrent.TimeUnit


internal object GrpcUtils {

    private var channel: GrpcChannel? = null


    @Synchronized
    fun getChannel(
        config: ChannelConfig
        ): GrpcChannel {
        val c = channel
        return if (c == null)
            getNewChannel(config)
        else
            if (c.equal(config) && c.isActive()) c
            else closeAndOpenChannel(config)
    }


    private fun closeAndOpenChannel(config: ChannelConfig): GrpcChannel {
        try {
            channel?.let {
                shutdownManagedChannel(it.channel)
            }
        }catch (e: Exception) {
            logger.error(
                "GrpcUtils",
                "closeAndOpenChannel: Exception - ${e.message}"
            )
        }

        return getNewChannel(config)
    }


    private fun getNewChannel(
        config: ChannelConfig
    ): GrpcChannel {

        channel = null
        val newChannel = GrpcChannel(config)
        channel = newChannel

        return newChannel
    }


    private fun shutdownManagedChannel(managedChannel: ManagedChannel) {
        if (channel?.channel == managedChannel) {
            channel = null
        }
        // Close the gRPC managed-channel if not shut down already.
        if (!managedChannel.isShutdown) {
            try {
                managedChannel.shutdown()
                if (!managedChannel.awaitTermination(1, TimeUnit.SECONDS)) {
                    logger.debug(
                        "GrpcUtils",
                        "awaitTermination: isShutdown - ${managedChannel.isShutdown}"
                    )
                }
            } catch (e: java.lang.Exception) {
                logger.error(
                    "GrpcUtils",
                    "shutdown: Exception - ${e.message}"
                )
            }
        }

        // Forceful shut down if still not terminated.
        if (!managedChannel.isTerminated) {
            try {
                managedChannel.shutdownNow()
                if (!managedChannel.awaitTermination(1, TimeUnit.SECONDS)) {
                    logger.debug(
                        "GrpcUtils",
                        "awaitTermination: isShutdown - ${managedChannel.isShutdown}"
                    )
                }
            } catch (e: java.lang.Exception) {
                logger.error(
                    "GrpcUtils",
                    "shutdownNow: Exception - ${e.message}"
                )
            }
        }
    }
}