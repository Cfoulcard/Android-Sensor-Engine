package com.christianfoulcard.android.androidsensorengine

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This generates the Hilt component and attaches itself to the the Application's lifecycle. This
 * means that this will provide dependencies to the Application.
 */
@HiltAndroidApp
class HiltInjection : Application() { }