package com.sensors.sensorMechanics

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class LightSensor(context: Context) : HardwareSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_LIGHT,
    sensorType = Sensor.TYPE_LIGHT
)

class PressureSensor(context: Context) : HardwareSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_BAROMETER,
    sensorType = Sensor.TYPE_PRESSURE
)
//
//class AmbientTemperatureSensor(context: Context) : HardwareSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE,
//    sensorType = Sensor.TYPE_AMBIENT_TEMPERATURE
//)
//
//class HumiditySensor(context: Context) : HardwareSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY,
//    sensorType = Sensor.TYPE_RELATIVE_HUMIDITY
//)