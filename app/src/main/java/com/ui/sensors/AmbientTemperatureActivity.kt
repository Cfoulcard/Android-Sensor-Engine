package com.ui.sensors

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
import com.ui.composables.CentralTemperatureGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstTemperatureInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondTemperatureInfoLabelGroup
import com.ui.composables.ThirdTemperatureInfoLabelGroup
import com.ui.sensors.viewmodels.AmbientTemperatureViewModel
import com.utils.UIUpdater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AmbientTemperatureActivity: BaseSensorActivity() {

    private val viewModel: AmbientTemperatureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    DisplaySensorTitle("Temperature Sensor")
                    InfoIcon(supportFragmentManager, this@AmbientTemperatureActivity, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralTemperatureGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "Temperature",
                            description = "hPa",
                            viewModel
                        )
                        FirstTemperatureInfoLabelGroup("Average Temperature", "0", viewModel)
                        SecondTemperatureInfoLabelGroup("Peak Temperature", "0", viewModel)
                        ThirdTemperatureInfoLabelGroup("Lowest Temperature", "0", viewModel)
                    }
                    PowerButton()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.startListening()
        UIUpdater().startUpdatingUI(500) { startLiveData() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListening()
        UIUpdater().stopUpdatingUI()
    }

    private fun startLiveData() {
        viewModel.averageTemperatureLiveData.postValue(viewModel.averageTemperatureReading())
        viewModel.highestTemperatureLiveData.postValue(viewModel.highestTemperatureReading())
        viewModel.lowestTemperatureLiveData.postValue(viewModel.lowestTemperatureReading())
    }

}