package com.ui.sensors.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.sensors.sensorMechanics.ObserveSensor
import com.sensors.sensorMechanics.SensorModule
import com.utils.SensorError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LightSensorViewModel @Inject constructor(
    @SensorModule.LightSensor private val lightSensor: ObserveSensor
): ViewModel() {

    val averageLightLiveData = MutableLiveData<String>("0")
    val highestLightLiveData = MutableLiveData<String>("0")
    val lowestLightLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0

    private var highestLux = Int.MIN_VALUE
    private var lowestLux = Int.MAX_VALUE

    var doesLightSensorExist = lightSensor.doesSensorExist
    var currentLux by mutableStateOf("0")

    fun startListening() {
        if (doesLightSensorExist) {
            lightSensor.startListening()
            lightSensor.setOnSensorValuesChangedListener { values ->
                val lux = values[0]
                val formatedLuxValue = lux.toInt() //"%.1f".format(lux)
                currentLux = formatedLuxValue.toString()
            }
        } else {
            SensorError().showNoSensorToast(globalAppContext)
        }
    }

    fun stopListening() {
        lightSensor.stopListening()
    }

    /** Obtains the average decibel reading we've obtained so far by dividing the sum of our decibels
     * by the amount of times decibel readings have occurred
     */
    fun averageLightReading(): String {
        addCurrentLux()
        return if (count != 0) {
            (sum / count).toString()
        } else {
            "0"
        }
    }

    private fun addCurrentLux() {
         count++
         sum += currentLux.toInt()
    }

    fun highestLightReading(): String {
        return if (highestLux == 0) {
            highestLux = currentLux.toInt()
            highestLux.toString()
        } else {
            highestLux = currentLux.toInt().let { Integer.max(highestLux, it) }
            highestLux.toString()
        }
    }

    fun lowestLightReading(): String {
        return if (lowestLux == 0) {
            lowestLux = currentLux.toInt()
            lowestLux.toString()
        } else {
            lowestLux = currentLux.toInt().let { Integer.min(lowestLux, it) }
            lowestLux.toString()
        }
    }
}
