package com.ui.sensors.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sensors.sensorMechanics.ObserveSensor
import com.sensors.sensorMechanics.SensorModule
import com.utils.SensorError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AmbientTemperatureViewModel @Inject constructor(
    @SensorModule.AmbientTemperatureSensor private val temperatureSensor: ObserveSensor,
    @ApplicationContext private val appContext: Context
): ViewModel() {

    val averageTemperatureLiveData = MutableLiveData<String>("0")
    val highestTemperatureLiveData = MutableLiveData<String>("0")
    val lowestTemperatureLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0

    private var highestTemperature = Int.MIN_VALUE
    private var lowestTemperature = Int.MAX_VALUE

    var doesTemperatureSensorExist = temperatureSensor.doesSensorExist
    var currentTemperature by mutableStateOf("0")

    fun startListening() {
        if (doesTemperatureSensorExist) {
            temperatureSensor.startListening()
            temperatureSensor.setOnSensorValuesChangedListener { values ->
                val temperature = values[0]
                val formatedTemperatureValue = temperature.toInt() //"%.1f".format(temperature)
                currentTemperature = formatedTemperatureValue.toString()
            }
        } else {
            SensorError().showNoSensorToast(appContext)
        }
    }

    fun stopListening() {
        temperatureSensor.stopListening()
    }

    fun averageTemperatureReading(): String {
        addCurrentTemperature()
        return if (count != 0) {
            (sum / count).toString()
        } else {
            "0"
        }
    }

    private fun addCurrentTemperature() {
        count++
        sum += currentTemperature.toInt()
    }

    fun highestTemperatureReading(): String {
        return if (highestTemperature == 0) {
            highestTemperature = currentTemperature.toInt()
            highestTemperature.toString()
        } else {
            highestTemperature = currentTemperature.toInt().let { Integer.max(highestTemperature, it) }
            highestTemperature.toString()
        }
    }

    fun lowestTemperatureReading(): String {
        return if (lowestTemperature == 0) {
            lowestTemperature = currentTemperature.toInt()
            lowestTemperature.toString()
        } else {
            lowestTemperature = currentTemperature.toInt().let { Integer.min(lowestTemperature, it) }
            lowestTemperature.toString()
        }
    }
}
