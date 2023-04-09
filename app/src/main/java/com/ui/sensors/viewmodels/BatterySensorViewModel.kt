package com.ui.sensors.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BatterySensorViewModel: ViewModel() {

    var batteryTemperature: Int = 0
    var batteryTemperatureName: String = "Celsius"

    var batteryPercentageLevel: Float = 0.0F
    var batteryVoltage: Int = 0
    var batteryHealth: String = "Unknown"

    val batteryPercentageLiveData = MutableLiveData<Float>(0.0F)
    val batteryVoltageLiveData = MutableLiveData<Int>(0)
    val batteryHealthLiveData = MutableLiveData<String>("")

}
