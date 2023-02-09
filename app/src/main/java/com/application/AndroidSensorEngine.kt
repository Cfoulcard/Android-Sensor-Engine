package com.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AndroidSensorEngine: Application() {

    override fun onCreate() {
        super.onCreate()
        initializeTimber()
    }

    /** Before the Timber library can be used, this must be started first */
    private fun initializeTimber() {
        Timber.plant(Timber.DebugTree())
    }
}