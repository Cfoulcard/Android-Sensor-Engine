package com.ui.sensors

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.composables.*
import com.ui.sensors.viewmodels.LightSensorViewModel
import com.utils.SystemUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class LightSensor: AppCompatActivity() {

    private val viewModel: LightSensorViewModel by viewModels()
    private var uiUpdaterJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUi().hideSystemUIFull(this)

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
                    DisplaySensorTitle("Light Sensor")
                    InfoIcon(supportFragmentManager, this@LightSensor, R.string.sound_desc_3)
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
                    PowerButton()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.startListening()

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListening()
    }
}