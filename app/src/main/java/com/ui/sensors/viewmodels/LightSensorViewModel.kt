package com.ui.sensors.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sensors.AudioDecibelManager
import com.sensors.sensorMechanics.ObserveSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LightSensorViewModel @Inject constructor(
    private val lightSensor: ObserveSensor
): ViewModel() {

    val averageLightLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0


    var doesLightSensorExist = lightSensor.doesSensorExist
    var currentLux by mutableStateOf("0")

    fun startListening() {
        lightSensor.startListening()
        lightSensor.setOnSensorValuesChangedListener { values ->
            val lux = values[0]
            val formatedLuxValue = "%.1f".format(lux)
            currentLux = formatedLuxValue
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
        return if (currentLux.toInt() == 0) {
            "0"
        } else {
            (sum / count).toString()
        }
    }

    private fun addCurrentLux() {
        count++
        sum += currentLux.toInt()
    }
}
