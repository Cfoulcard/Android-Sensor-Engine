package com.ui.sensors.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sensors.sensorMechanics.ObserveSensor
import com.sensors.sensorMechanics.SensorModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PressureSensorViewModel @Inject constructor(
    @SensorModule.PressureSensor private val pressureSensor: ObserveSensor
): ViewModel() {

    val averagePressureLiveData = MutableLiveData<String>("0")
    val highestPressureLiveData = MutableLiveData<String>("0")
    val lowestPressureLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0

    private var highestPressure = Int.MIN_VALUE
    private var lowestPressure = Int.MAX_VALUE

    var doesPressureSensorExist = pressureSensor.doesSensorExist
    var currentPressure by mutableStateOf("0")

    fun startListening() {
        pressureSensor.startListening()
        pressureSensor.setOnSensorValuesChangedListener { values ->
            val pressure = values[0]
            val formatedPressureValue = pressure.toInt() //"%.1f".format(pressure)
            currentPressure = formatedPressureValue.toString()
        }
    }

    fun stopListening() {
        pressureSensor.stopListening()
    }

    fun averagePressureReading(): String {
        addCurrentPressure()
        return if (count != 0) {
            (sum / count).toString()
        } else {
            "0"
        }
    }

    private fun addCurrentPressure() {
        count++
        sum += currentPressure.toInt()
    }

    fun highestPressureReading(): String {
        return if (highestPressure == 0) {
            highestPressure = currentPressure.toInt()
            highestPressure.toString()
        } else {
            highestPressure = currentPressure.toInt().let { Integer.max(highestPressure, it) }
            highestPressure.toString()
        }
    }

    fun lowestPressureReading(): String {
        return if (lowestPressure == 0) {
            lowestPressure = currentPressure.toInt()
            lowestPressure.toString()
        } else {
            lowestPressure = currentPressure.toInt().let { Integer.min(lowestPressure, it) }
            lowestPressure.toString()
        }
    }
}
