package com.sensors.sensorMechanics

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.androidsensorengine.utils.LogUtils.TAG
import timber.log.Timber

abstract class HardwareSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
) : ObserveSensor(sensorType), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    override fun startListening() {
        if(!doesSensorExist) {
            return
        }
        if (!::sensorManager.isInitialized && sensor == null) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun stopListening() {
        if (!doesSensorExist || ::sensorManager.isInitialized) {
            return
        }
        sensorManager.unregisterListener(this)
        Timber.tag(TAG).d("Stopped listening")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!doesSensorExist) {
            return
        }
        if (event?.sensor?.type == sensorType) {
            onSensorValuesChanged?.invoke(event.values.toList())
        }
        Timber.tag(TAG).d("Sensor changed")

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Timber.tag(TAG).d("onAccuracyChanged: ")
    }

}