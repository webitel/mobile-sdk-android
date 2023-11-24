package com.webitel.mobile_sdk.data.repository

import android.os.Build
import com.webitel.mobile_sdk.data.repository.storage.DeviceInfoStorage
import java.util.UUID


internal class DeviceInfoRepository(
    private val storage: DeviceInfoStorage
) {

    fun getDeviceId(): String {
        var savedId = storage.getDeviceId()

        if (savedId.isNullOrEmpty()) {
            savedId = generateDeviceId()
            storage.saveDeviceId(savedId)
        }

        return savedId
    }


    fun getUserAgent(appName: String, appVersion: String): String {
        val result = StringBuilder(64)
        result.append("$appName/")
        result.append(appVersion) // such as 1.1.0 System.getProperty("java.vm.version")
        result.append(" (Linux; U; Android ")
        val version = Build.VERSION.RELEASE
        result.append(version.ifEmpty { "1.0" })

        // add the model for the release build
        if ("REL" == Build.VERSION.CODENAME) {
            val model = Build.MODEL
            if (model.isNotEmpty()) {
                result.append("; ")
                result.append(model)
            }
        }

        val id = Build.ID
        if (id.isNotEmpty()) {
            result.append(" Build/")
            result.append(id)
        }
        result.append(")")

        return result.toString()
    }


    private fun generateDeviceId(): String {
        return UUID.randomUUID().toString()
    }
}