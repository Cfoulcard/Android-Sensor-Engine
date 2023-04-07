package com.sensors.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.androidsensorengine.utils.LogUtils.TAG
import timber.log.Timber

class BatteryInfoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            val batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            val temperatureInCelsius = batteryTemperature.toFloat() / 10
            val temperatureInFahrenheit = temperatureInCelsius * 9 / 5 + 32
            val temperatureInKelvin = temperatureInCelsius + 273

            Timber.d(TAG, "Celsius is $temperatureInCelsius")
            Timber.d(TAG, "Fahrenheit is $temperatureInFahrenheit")
            Timber.d(TAG, "Kelvin is $temperatureInKelvin")
            
        }
    }
}
