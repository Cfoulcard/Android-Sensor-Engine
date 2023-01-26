package com.androidsensorengine.ui.sensors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.androidsensorengine.ui.theme.HomeScreenShapes
import com.christianfoulcard.android.androidsensorengine.R

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
                        Group()
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
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back_circle_dark),
                contentDescription = "backCircle",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(370.dp).blur(8.dp).alpha(0.75f),
                alignment = Alignment.Center,
            )
            Image(
                painter = painterResource(R.drawable.ic_back_circle),
                contentDescription = "backCircle",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(360.dp),
                alignment = Alignment.Center,
            )
            Image(
                painter = painterResource(R.drawable.ic_top_circle_dark),
                contentDescription = "topCircle",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(260.dp).blur(16.dp).alpha(.90f),
                alignment = Alignment.Center,
            )
            Image(
                painter = painterResource(R.drawable.ic_top_circle),
                contentDescription = "topCircle",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp),
                alignment = Alignment.Center,
            )
            Column(
                modifier = Modifier.absoluteOffset(4.dp, (-4).dp)
            ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontSize = 48.sp)) { append("75") }
                            withStyle(style = SpanStyle(
                                fontSize = 14.sp,
                                baselineShift = BaselineShift(+1f)
                            )) { append("db") }
                        },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h3,

                    )
                Text(
                    text = "Loudness",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.absoluteOffset(y = (-26).dp)
                )
            }
        }

    }

    @Composable
    fun Group() {
        Card(
            elevation = 24.dp,
            modifier = Modifier
                .width(width = 272.dp)
                .height(height = 44.dp),
            shape = HomeScreenShapes.small
        ) {
            Box(contentAlignment = Alignment.Center) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Average Decibel Reading",
                        color = Color(0xff292929),
                        textAlign = TextAlign.Left,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
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
                DisplayTitle()
                InfoIcon()

                Column(modifier = Modifier.padding(top = 90.dp)) {
                    TopCircle()
                    Group()
                }
            }
        }
    }
}