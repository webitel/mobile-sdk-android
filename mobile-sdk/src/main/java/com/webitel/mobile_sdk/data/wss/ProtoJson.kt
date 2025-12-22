package com.webitel.mobile_sdk.data.wss

import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat
import com.google.rpc.ErrorInfo
import errors.ErrorOuterClass
import webitel.chat.DialogOuterClass
import webitel.portal.Auth
import webitel.portal.Connect
import webitel.portal.CustomerOuterClass
import webitel.portal.Messages


object ProtoJson {

    private val registry = JsonFormat.TypeRegistry.newBuilder()
        .add(Connect.Request.getDescriptor())
        .add(Connect.Response.getDescriptor())
        .add(DialogOuterClass.ChatDialogsRequest.getDescriptor())
        .add(Messages.SendMessageRequest.getDescriptor())
        .add(Messages.UpdateNewMessage.getDescriptor())
        .add(Messages.ChatDialogsRequest.getDescriptor())
        .add(ErrorOuterClass.Error.getDescriptor())
        .add(Auth.AccessToken.getDescriptor())
        .add(Auth.TokenRequest.getDescriptor())
        .add(CustomerOuterClass.InspectRequest.getDescriptor())
        .add(ErrorInfo.getDescriptor())
        .add(Connect.Update.getDescriptor())
        .build()


    private val parser = JsonFormat.parser()
        .ignoringUnknownFields()
        .usingTypeRegistry(registry)

    private val printer = JsonFormat.printer()
        .includingDefaultValueFields()
        .usingTypeRegistry(registry)

    fun toJson(message: Message): String =
        printer.print(message)

    fun fromJson(json: String, builder: Connect.Update.Builder): Connect.Update =
        parser.merge(json, builder).let { builder.build() }

    fun fromJson(json: String, builder: Auth.AccessToken.Builder): Auth.AccessToken =
        parser.merge(json, builder).let { builder.build() }
}