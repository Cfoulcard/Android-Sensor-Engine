package com.utils

import kotlinx.coroutines.*
import java.util.*

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

    fun startUpdatingUIWithTimer(callback: () -> Unit) {
                val timer = Timer()

                val uiTimer: TimerTask = object: TimerTask() {
                    override fun run() {
                        callback()
                    }

                }
        timer.schedule(uiTimer, 1000, 1000)
    }
}