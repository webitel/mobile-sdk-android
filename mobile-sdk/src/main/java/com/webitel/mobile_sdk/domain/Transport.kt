package com.webitel.mobile_sdk.domain


/**
 * Client transport type.
 */
enum class Transport {
    /** gRPC transport (default). */
    GRPC,

    /** WebSocket transport. */
    WEBSOCKET
}