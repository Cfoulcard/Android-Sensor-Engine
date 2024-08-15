package com.application

import android.app.Application
import android.content.Context
import apolloClient
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AndroidSensorEngine: Application() {

    override fun onCreate() {
        super.onCreate()
        globalAppContext = applicationContext
        initializeTimber()
        apolloClient
    }

    /** Before the Timber library can be used, this must be started first */
    private fun initializeTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var globalAppContext: Context
            private set
    }
}