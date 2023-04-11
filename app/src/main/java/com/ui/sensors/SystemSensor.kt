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
import com.ui.sensors.viewmodels.SystemSensorViewModel
import com.utils.UIUpdater

class SystemSensor : BaseSensorActivity() {

    private val viewModel: SystemSensorViewModel by viewModels()

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
                    DisplaySensorTitle("System Sensor")
                    InfoIcon(supportFragmentManager, this@SystemSensor, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralSystemGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "GB",
                            description = "Available Ram",
                            viewModel
                        )
                        FirstSystemInfoLabelGroup("Total Ram", "0", viewModel)
                        SecondSystemInfoLabelGroup("Threshold", "0", viewModel)
                     //   ThirdSystemInfoLabelGroup("Lowest Lux", "0", viewModel)
                    }
                    PowerButton()
                }
            }
        }

    }



    override fun onResume() {
        super.onResume()
        viewModel.startPeriodicUpdates()
        UIUpdater().startUpdatingUI(500) {
            startLiveData()
        }
    }



    override fun onPause() {
        super.onPause()
        viewModel.cancelPeriodicUpdates()
        UIUpdater().stopUpdatingUI()
    }

    private fun startLiveData() {
        viewModel.availableMemoryLiveData.postValue(viewModel.availableMemory)
        viewModel.totalMemoryLiveData.postValue(viewModel.totalMemory)
        viewModel.thresholdMemoryLiveData.postValue(viewModel.thresholdMemory)
    }

}