package com.ui.sensors.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidsensorengine.utils.Constants.MOIST_HUMID_PREFS
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.preferences.AppSharedPrefs
import com.sensors.sensorMechanics.ObserveSensor
import com.sensors.sensorMechanics.SensorModule
import com.utils.SensorError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HumiditySensorViewModel @Inject constructor(
    @SensorModule.HumiditySensor private val humiditySensor: ObserveSensor
): ViewModel() {

    val averageHumidityLiveData = MutableLiveData<String>("0")
    val highestHumidityLiveData = MutableLiveData<String>("0")
    val lowestHumidityLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0

    private var highestHumidity = Int.MIN_VALUE
    private var lowestHumidity = Int.MAX_VALUE

    var doesHumiditySensorExist = humiditySensor.doesSensorExist
    var currentHumidity by mutableStateOf("0")

    fun startListening() {
        if (doesHumiditySensorExist) {
            AppSharedPrefs().saveCondition(MOIST_HUMID_PREFS, true)
            humiditySensor.startListening()
            humiditySensor.setOnSensorValuesChangedListener { values ->
                val lux = values[0]
                val formattedHumidityValue = lux.toInt() //"%.1f".format(lux)
                currentHumidity = formattedHumidityValue.toString()
            }
        } else {
            SensorError().showNoSensorToast(globalAppContext)
        }
    }

    fun stopListening() {
        humiditySensor.stopListening()
    }

    /** Obtains the average decibel reading we've obtained so far by dividing the sum of our decibels
     * by the amount of times decibel readings have occurred
     */
    fun averageHumidityReading(): String {
        addCurrentHumidity()
        return if (count != 0) {
            (sum / count).toString()
        } else {
            "0"
        }
    }

    private fun addCurrentHumidity() {
        count++
        sum += currentHumidity.toInt()
    }

    fun highestHumidityReading(): String {
        return if (highestHumidity == 0) {
            highestHumidity = currentHumidity.toInt()
            highestHumidity.toString()
        } else {
            highestHumidity = currentHumidity.toInt().let { Integer.max(highestHumidity, it) }
            highestHumidity.toString()
        }
    }

    fun lowestHumidityReading(): String {
        return if (lowestHumidity == 0) {
            lowestHumidity = currentHumidity.toInt()
            lowestHumidity.toString()
        } else {
            lowestHumidity = currentHumidity.toInt().let { Integer.min(lowestHumidity, it) }
            lowestHumidity.toString()
        }
    }
}