package com.application

import android.app.Application
import timber.log.Timber

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