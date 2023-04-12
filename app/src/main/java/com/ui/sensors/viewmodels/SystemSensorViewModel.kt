package com.ui.sensors.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.sensors.system.SystemInfo

class SystemSensorViewModel: ViewModel() {

    var availableMemory = "0"
    var totalMemory = "0"
    var thresholdMemory = "0"

    val totalMemoryLiveData = MutableLiveData<String>("")
    val availableMemoryLiveData = MutableLiveData<String>("")
    val thresholdMemoryLiveData = MutableLiveData<String>("")

    fun updateUiWithMemoryInfo()  {
            SystemInfo(globalAppContext).getMemoryInfo()
            availableMemory = SystemInfo(globalAppContext).getAvailableMemory()
            totalMemory = SystemInfo(globalAppContext).getTotalMemory()
            thresholdMemory = SystemInfo(globalAppContext).getThresholdMemory()
    }

//    fun startPeriodicUpdates(delay: Long) : Job {
//
//        return coroutineScope.launch {
//            while (isActive) {
//                SystemInfo(globalAppContext).getMemoryInfo()
//                availableMemory = SystemInfo(globalAppContext).getAvailableMemory()
//                totalMemory = SystemInfo(globalAppContext).getTotalMemory()
//                thresholdMemory = SystemInfo(globalAppContext).getThresholdMemory()
//                delay(delay)
//            }
//        }
//    }

}
