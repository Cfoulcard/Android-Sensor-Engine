package com.ui.sensors.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sensors.sensorMechanics.ObserveSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LightSensorViewModel @Inject constructor(
    private val lightSensor: ObserveSensor
): ViewModel() {

    var doesLightSensorExist = lightSensor.doesSensorExist
    var currentLux by mutableStateOf("0")

    fun startListening() {
        lightSensor.startListening()
        lightSensor.setOnSensorValuesChangedListener { values ->
            val lux = values[0]
            currentLux = lux.toString()
        }
    }

    fun stopListening() {
        lightSensor.stopListening()
    }
}
