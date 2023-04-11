package com.sensors.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryInfoReceiver(private val listener: BatteryInfoListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            getBatteryTemperature(intent)
            getMaximumBatteryLevelPercentage(intent)
            getBatteryVoltage(intent)
            getBatteryHealth(intent)
            getCurrentBatteryStatusType(intent)
            getBatteryTechnologyInfo(intent)
            getPluggedInType(intent)
        }
    }

    private fun getBatteryTemperature(intent: Intent) {
        val batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val temperatureInCelsius = batteryTemperature.toFloat() / 10
        val temperatureInFahrenheit = temperatureInCelsius * 9 / 5 + 32
        val temperatureInKelvin = temperatureInCelsius + 273

        listener.onBatteryTemperatureUpdated(temperatureInFahrenheit)
        listener.retrieveBatteryTemperatureName("Fahrenheit")
    }

    private fun getMaximumBatteryLevelPercentage(intent: Intent) {
        val batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val batteryPercentage = batteryLevel.toFloat() / batteryScale.toFloat() * 100
        listener.onBatteryLevelPercentageUpdated(batteryPercentage)
    }

    private fun getBatteryVoltage(intent: Intent) {
        val batteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        listener.onBatteryVoltageUpdated(batteryVoltage)
    }

    private fun getBatteryHealth(intent: Intent) {
        val healthString = when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)) {
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over voltage"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified failure"
            else -> "Unknown"
        }
        listener.onBatteryHealthUpdated(healthString)
    }

    private fun getCurrentBatteryStatusType(intent: Intent) {
        val statusString = when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not charging"
            BatteryManager.BATTERY_STATUS_UNKNOWN -> "Unknown"
            else -> "Unknown"
        }
        listener.onBatteryStatusUpdated(statusString)
    }

    private fun getBatteryTechnologyInfo(intent: Intent) {
        val batteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        if (batteryTechnology != null) {
            listener.retrieveBatteryTechnologyInfo(batteryTechnology)
        } else {
            listener.retrieveBatteryTechnologyInfo("Unknown")
        }
    }

    private fun getPluggedInType(intent: Intent) {
        val pluggedString = when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            BatteryManager.BATTERY_PLUGGED_AC -> "Plugged to AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "Plugged to USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Plugged to Wireless"
            0 -> "Not plugged"
            else -> "Unknown"
        }
        listener.onPluggedUpdated(pluggedString)
    }
}
