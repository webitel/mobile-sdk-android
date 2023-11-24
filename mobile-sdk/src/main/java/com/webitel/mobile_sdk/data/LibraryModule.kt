package com.webitel.mobile_sdk.data

import android.app.Application


internal object LibraryModule {

    @Volatile
    lateinit var application: Application

    fun initializeDI(_application: Application) {
        if (!LibraryModule::application.isInitialized) {
            synchronized(this) {
                application = _application
            }
        }
    }
}