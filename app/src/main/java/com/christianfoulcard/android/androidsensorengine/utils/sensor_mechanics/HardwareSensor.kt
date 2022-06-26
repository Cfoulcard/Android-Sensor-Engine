package com.christianfoulcard.android.androidsensorengine.utils.sensor_mechanics

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.christianfoulcard.android.androidsensorengine.utils.LogUtils.TAG

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
        }
    }

    override fun stopListening() {
        Log.d(TAG, "stopListening: ")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d(TAG, "onSensorChanged: ")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged: ")
    }

}