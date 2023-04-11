package com.ui.sensors.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.sensors.system.SystemInfo
import kotlinx.coroutines.*

class SystemSensorViewModel: ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun startPeriodicUpdates() {
        val systemInfo = SystemInfo(globalAppContext)
        var availableMemoryTextView = ""

        coroutineScope.launch {
            while (isActive) {
            //    val availableMemoryGB = systemInfo.getAvailableMemory()
                val memoryInfoRefresh = SystemInfo(globalAppContext).getMemoryInfo()
                val availableMemory = SystemInfo(globalAppContext).getAvailableMemory()
                val totalMemory = SystemInfo(globalAppContext).getTotalMemory()
                val thresholdMemory = SystemInfo(globalAppContext).getThresholdMemory()
                delay(1000) // Update every 1000 ms (1 second)
            }
        }
    }

    fun cancelPeriodicUpdates() {
        coroutineScope.cancel()
    }

    val totalMemoryLiveData = MutableLiveData<String>("")
    val availableMemoryLiveData = MutableLiveData<String>("")
    val thresholdMemoryLiveData = MutableLiveData<String>("")

    val memoryInfoRefresh = SystemInfo(globalAppContext).getMemoryInfo()
    val availableMemory = SystemInfo(globalAppContext).getAvailableMemory()
    val totalMemory = SystemInfo(globalAppContext).getTotalMemory()
    val thresholdMemory = SystemInfo(globalAppContext).getThresholdMemory()
}
