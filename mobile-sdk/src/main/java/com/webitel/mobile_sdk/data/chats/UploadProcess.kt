package com.webitel.mobile_sdk.data.chats

import io.grpc.stub.ClientCallStreamObserver
import webitel.portal.Media


// Class representing an active upload process
internal data class UploadProcess(val request: UploadRequest) {
    private var call: ClientCallStreamObserver<Media.UploadRequest>? = null
    private var pid = ""

    private var isCompleted = false
    private var reported = false

    fun isCompleted(): Boolean {
        return isCompleted
    }

    fun completed() {
        isCompleted = true
    }


    fun setReported() {
        reported = true
    }


    fun setPid(pid: String) {
        this.pid = pid
    }


    fun getPid(): String {
        return pid
    }


    fun isReported(): Boolean {
        return reported
    }


    fun getStream(): ClientCallStreamObserver<Media.UploadRequest>? {
        return call
    }


    fun setStream(call: ClientCallStreamObserver<Media.UploadRequest>?) {
        this.call = call
    }
}