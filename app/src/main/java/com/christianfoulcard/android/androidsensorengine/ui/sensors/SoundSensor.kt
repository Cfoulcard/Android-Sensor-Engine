package com.christianfoulcard.android.androidsensorengine.ui.sensors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.christianfoulcard.android.androidsensorengine.ui.composables.MainGradientBackground
import com.christianfoulcard.android.androidsensorengine.ui.composables.SensorCometBackground
import com.christianfoulcard.android.androidsensorengine.ui.theme.AndroidSensorEngineTheme

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
                    DisplayTitle()
                    InfoIcon()
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        TopCircle()
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayTitle() {
        Text(
            text = "Sound Sensor",
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp),
            style = MaterialTheme.typography.h1
        )
    }

    @Composable
    fun InfoIcon() {
        Image(
            painter = painterResource(R.drawable.ic_icon_info),
            contentDescription = "info",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(32.dp)
                .fillMaxSize(),
            alignment = Alignment.Center,
            )
    }

    @Composable
    fun TopCircle() {
        Image(
            painter = painterResource(R.drawable.ic_top_circle),
            contentDescription = "topCircle",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(220.dp)
                .fillMaxSize(),
            alignment = Alignment.Center,
        )
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
                DisplayTitle()
                InfoIcon()
                Column(modifier = Modifier.padding(top = 90.dp)) {
                    TopCircle()
                }
            }
        }
    }
}