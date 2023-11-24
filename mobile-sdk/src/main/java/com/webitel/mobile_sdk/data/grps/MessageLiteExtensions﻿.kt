package com.webitel.mobile_sdk.data.grps

import com.google.protobuf.Any
import com.google.protobuf.Internal
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.MessageLite
import java.util.Objects


@Throws(InvalidProtocolBufferException::class)
fun <T : MessageLite> Any.unpack(clazz: Class<T>): T? {
    val defaultInstance = Internal.getDefaultInstance(clazz)
    return try {
        @Suppress("UNCHECKED_CAST")
        defaultInstance.parserForType.parseFrom(value) as T
    } catch (e: java.lang.Exception) {
        null
    }
}


fun <T : MessageLite> Any.Builder.pack(message: T): Any {
    val packageName =
        Objects.requireNonNull(message.javaClass.getPackage()).name
    return this.setTypeUrl("type.googleapis.com/$packageName." + message.javaClass.simpleName)
        .setValue(message.toByteString())
        .build()
}


fun <T : MessageLite> Any.`is`(clazz: Class<T>): Boolean {
    val packageName =
        Objects.requireNonNull(clazz.getPackage()).name
    return this.typeUrl == "type.googleapis.com/$packageName.${clazz.simpleName}"
}