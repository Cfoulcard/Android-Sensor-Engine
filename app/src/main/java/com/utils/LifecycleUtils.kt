package com.utils

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object LifecycleUtils {

    /** The desired callback will begin in the appropiate Android lifecycle component.
     */
    fun LifecycleCoroutineScope.startUpdatingUiWithMainCoroutine(delay: Long, callback: () -> Unit): Job {
        return launch {
            while (isActive) {
                callback()
                kotlinx.coroutines.delay(delay)
            }
        }
    }
}