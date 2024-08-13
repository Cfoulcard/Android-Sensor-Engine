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
import com.ui.composables.CentralLightGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstLightInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondLightInfoLabelGroup
import com.ui.composables.ThirdLightInfoLabelGroup
import com.ui.sensors.viewmodels.LightSensorViewModel
import com.utils.UIUpdater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LightSensor: BaseSensorActivity() {

    private val viewModel: LightSensorViewModel by viewModels()

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
                    DisplaySensorTitle("Light Sensor")
                    InfoIcon(supportFragmentManager, this@LightSensor, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralLightGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "lux",
                            description = "Brightness",
                            viewModel
                        )
                        FirstLightInfoLabelGroup("Average Lux", "0", viewModel)
                        SecondLightInfoLabelGroup("Peak Lux", "0", viewModel)
                        ThirdLightInfoLabelGroup("Lowest Lux", "0", viewModel)
                    }
                   // PowerButton()
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
        viewModel.averageLightLiveData.postValue(viewModel.averageLightReading())
        viewModel.highestLightLiveData.postValue(viewModel.highestLightReading())
        viewModel.lowestLightLiveData.postValue(viewModel.lowestLightReading())
    }

}