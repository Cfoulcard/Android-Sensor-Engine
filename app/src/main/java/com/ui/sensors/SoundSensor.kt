package com.androidsensorengine.ui.sensors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.ui.composables.*

class SoundSensor: ComponentActivity() {

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
                    DisplaySensorTitle("Sound Sensor")
                    InfoIcon()
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralGraphicSensorInfo(
                            largeInfoString = "75",
                            superScript = "db",
                            description = "Loudness"
                        )
                            FirstInfoLabelGroup("Average Decibel Reading", "67")
                            SecondInfoLabelGroup("Peak Loudness", "85")
                            ThirdInfoLabelGroup("Lowest Decibel", "27")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
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
                DisplaySensorTitle("Sound Sensor")
                InfoIcon()
                Column(modifier = Modifier.padding(top = 90.dp)) {
                    CentralGraphicSensorInfo(
                        largeInfoString = "75",
                        superScript = "db",
                        description = "Loudness"
                    )
                    FirstInfoLabelGroup("Average Decibel Reading", "67")
                    SecondInfoLabelGroup("Peak Loudness", "85")
                    ThirdInfoLabelGroup("Lowest Decibel", "27")
                }
            }
        }
    }
}