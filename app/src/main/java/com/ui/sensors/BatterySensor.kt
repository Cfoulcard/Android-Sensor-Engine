package com.ui.sensors

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.BaseSensorActivity
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.christianfoulcard.android.androidsensorengine.R
import com.sensors.battery.BatteryInfoListener
import com.sensors.battery.BatteryInfoReceiver
import com.ui.composables.*
import com.ui.sensors.viewmodels.BatterySensorViewModel

class BatterySensor: BaseSensorActivity(), BatteryInfoListener {

    private val viewModel: BatterySensorViewModel by viewModels()
    private lateinit var batteryInfoReceiver: BatteryInfoReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryInfoReceiver = BatteryInfoReceiver(this)

        setContent {

            AndroidSensorEngineTheme {

                MainGradientBackground()
                HalfCircleBackgroundLonger()
                SensorCometBackground()
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    DisplaySensorTitle("Battery Sensor")
                    InfoIcon(supportFragmentManager, this@BatterySensor, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralBatteryGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "°",
                            description = "Celsius",
                            viewModel
                        )
                        FirstBatteryInfoLabelGroup("Current %", "0", viewModel)
                        SecondBatteryInfoLabelGroup("Battery Voltage", "0", viewModel)
                        ThirdBatteryInfoLabelGroup("Battery Health", "Unknown", viewModel)
                    }
                    PowerButton()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryInfoReceiver)
    }

    override fun retrieveBatteryTemperatureName(name: String) {
        viewModel.batteryTemperatureName = name
    }

    override fun onBatteryTemperatureUpdated(temperature: Float) {
        viewModel.batteryTemperature = temperature.toInt()
    }

    override fun onBatteryLevelPercentageUpdated(batteryPercent: Float) {
        viewModel.batteryPercentageLiveData.postValue(batteryPercent)
    }

    override fun onBatteryVoltageUpdated(voltage: Int) {
        viewModel.batteryVoltageLiveData.postValue(voltage)
    }

    override fun onBatteryHealthUpdated(health: String) {
        viewModel.batteryHealthLiveData.postValue(health)
    }


}