package com.sensors.battery

interface BatteryInfoListener {
    fun retrieveBatteryTemperatureName(name: String)
    fun onBatteryTemperatureUpdated(temperature: Float)
    fun onBatteryLevelPercentageUpdated(batteryPercent: Float)
    fun onBatteryVoltageUpdated(voltage: Int)
    fun onBatteryHealthUpdated(health: String)
}
