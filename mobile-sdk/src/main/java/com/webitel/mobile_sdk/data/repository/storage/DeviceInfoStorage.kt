package com.webitel.mobile_sdk.data.repository.storage

internal interface DeviceInfoStorage {
    fun getDeviceId(): String?

    fun saveDeviceId(id: String)

    fun clear()
}