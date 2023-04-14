package com.ui.sensors

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
import com.ui.composables.*
import com.ui.sensors.viewmodels.HumiditySensorViewModel
import com.utils.UIUpdater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HumiditySensor: BaseSensorActivity() {

    private val viewModel: HumiditySensorViewModel by viewModels()

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
                        .fillMaxSize()
                ) {
                    DisplaySensorTitle("Humidity Sensor")
                    InfoIcon(supportFragmentManager, this@HumiditySensor, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralHumidityGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "%",
                            description = "",
                            viewModel
                        )
                        FirstHumidityInfoLabelGroup("Average Humidity", "0", viewModel)
                        SecondHumidityInfoLabelGroup("Peak Humidity", "0", viewModel)
                        ThirdHumidityInfoLabelGroup("Lowest Humidity", "0", viewModel)
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
        viewModel.averageHumidityLiveData.postValue(viewModel.averageHumidityReading())
        viewModel.highestHumidityLiveData.postValue(viewModel.highestHumidityReading())
        viewModel.lowestHumidityLiveData.postValue(viewModel.lowestHumidityReading())
    }

}