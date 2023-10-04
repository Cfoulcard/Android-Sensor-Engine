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
import com.ui.composables.CentralPressureGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstPressureInfoLabelGroup
import com.ui.composables.FourthPressureInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondPressureInfoLabelGroup
import com.ui.composables.ThirdPressureInfoLabelGroup
import com.ui.sensors.viewmodels.PressureSensorViewModel
import com.utils.UIUpdater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PressureSensor: BaseSensorActivity() {

    private val viewModel: PressureSensorViewModel by viewModels()

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
                    DisplaySensorTitle("Pressure and Altitude Sensor")
                    InfoIcon(supportFragmentManager, this@PressureSensor, R.string.pressure_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralPressureGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "Pressure",
                            description = "hPa",
                            viewModel
                        )
                        FirstPressureInfoLabelGroup("Average Pressure", "0", viewModel)
                        SecondPressureInfoLabelGroup("Peak Pressure", "0", viewModel)
                        ThirdPressureInfoLabelGroup("Lowest Pressure", "0", viewModel)
                        FourthPressureInfoLabelGroup("Altitude", "0", viewModel)
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
        viewModel.averagePressureLiveData.postValue(viewModel.averagePressureReading())
        viewModel.highestPressureLiveData.postValue(viewModel.highestPressureReading())
        viewModel.lowestPressureLiveData.postValue(viewModel.lowestPressureReading())
        viewModel.altitudeLiveData.postValue(viewModel.currentAltitude)
    }

}