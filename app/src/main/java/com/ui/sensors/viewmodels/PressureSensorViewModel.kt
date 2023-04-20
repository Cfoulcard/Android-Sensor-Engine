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
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class PressureSensorViewModel @Inject constructor(
    @SensorModule.PressureSensor private val pressureSensor: ObserveSensor
): ViewModel() {

    val averagePressureLiveData = MutableLiveData<String>("0")
    val highestPressureLiveData = MutableLiveData<String>("0")
    val lowestPressureLiveData = MutableLiveData<String>("0")
    val altitudeLiveData = MutableLiveData<String>("0")

    private var count = 0
    private var sum = 0

    private var highestPressure = Int.MIN_VALUE
    private var lowestPressure = Int.MAX_VALUE

    var doesPressureSensorExist = pressureSensor.doesSensorExist
    var currentPressure by mutableStateOf("0")
    var currentAltitude by mutableStateOf("0")

    fun startListening() {
        if (doesPressureSensorExist) {
            pressureSensor.startListening()
            pressureSensor.setOnSensorValuesChangedListener { values ->
                val pressure = values[0]
                val formattedPressureValue = pressure.toInt() //"%.1f".format(pressure)
                currentPressure = formattedPressureValue.toString()
                currentAltitude = calculateAltitude(pressure)
            }
        } else {
            SensorError().showNoSensorToast(globalAppContext)
        }
    }

    fun stopListening() {
        pressureSensor.stopListening()
    }

    /** This formula uses the info from the following link:
     * https://community.bosch-sensortec.com/t5/Question-and-answers/How-to-calculate-the-altitude-from-the-pressure-sensor-data/qaq-p/5702
     */
    private fun calculateAltitude(pressure: Float, seaLevelPressure: Float = 1013.25f): String {
        if (pressure <= 0 || seaLevelPressure <= 0 || pressure >= 100000) {
            return "N/A"
        }

        val altitude = 44330.0 * (1.0 - (pressure / seaLevelPressure).toDouble().pow(1.0 / 5.255))
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(altitude)
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
