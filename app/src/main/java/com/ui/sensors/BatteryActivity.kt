package com.ui.sensors

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
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
import com.ui.composables.CentralBatteryGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FifthBatteryInfoLabelGroup
import com.ui.composables.FirstBatteryInfoLabelGroup
import com.ui.composables.FourthBatteryInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondBatteryInfoLabelGroup
import com.ui.composables.SixthBatteryInfoLabelGroup
import com.ui.composables.ThirdBatteryInfoLabelGroup
import com.ui.sensors.viewmodels.BatterySensorViewModel

class BatteryActivity: BaseSensorActivity(), BatteryInfoListener {

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
                        .fillMaxSize().verticalScroll(enabled = true, state = ScrollState(initial = -1))
                ) {
                    DisplaySensorTitle("Battery Sensor")
                    InfoIcon(supportFragmentManager, this@BatteryActivity, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralBatteryGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "°",
                            description = "Celsius",
                            viewModel
                        )
                        FirstBatteryInfoLabelGroup("Current %", "0", viewModel)
                        SecondBatteryInfoLabelGroup("Voltage", "0", viewModel)
                        ThirdBatteryInfoLabelGroup("Health", "Unknown", viewModel)
                        FourthBatteryInfoLabelGroup("Status", "Unknown", viewModel)
                        SixthBatteryInfoLabelGroup("Plug Status", "Unknown", viewModel)
                        FifthBatteryInfoLabelGroup("Technology", "Unknown", viewModel)
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

    override fun onBatteryStatusUpdated(status: String) {
        viewModel.batteryStatusLiveData.postValue(status)
    }

    override fun retrieveBatteryTechnologyInfo(technology: String) {
        viewModel.batteryTechnologyLiveData.postValue(technology)
    }

    override fun onPluggedUpdated(pluggedString: String) {
        viewModel.batteryPluggedLiveData.postValue(pluggedString)
    }


}