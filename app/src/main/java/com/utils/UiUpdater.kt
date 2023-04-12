package com.utils

import kotlinx.coroutines.*

class UIUpdater() {
    private var uiUpdaterJob: Job? = null

    fun startUpdatingUI(updateMilli: Long, callback: () -> Unit) {
        uiUpdaterJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(updateMilli)
                callback()
            }
        }
    }

    fun stopUpdatingUI() {
        uiUpdaterJob?.cancel()
        uiUpdaterJob = null
    }

}