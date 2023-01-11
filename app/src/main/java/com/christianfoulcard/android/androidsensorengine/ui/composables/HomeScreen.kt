package com.christianfoulcard.android.androidsensorengine.ui.composables

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.ui.sensors.SoundSensor
import com.christianfoulcard.android.androidsensorengine.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SensorIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = {navigateToSoundSensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize(),
            alignment = Alignment.Center,

        )
    }
}

fun navigateToSoundSensor(context: Context) {
    context.startActivity(Intent(context, SoundSensor::class.java))
    }

@Composable
fun SensorCometBackground() {
    Image(
        painter = painterResource(R.drawable.ic_comet_gradient),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxHeight(0.4f).fillMaxWidth(1f).fillMaxSize(),
        alignment = Alignment.Center,
        )
}


@Composable
fun SensorDiagnosisRow(text: String, drawable: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.6f),
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.h1,
        )
        Image(
            painter = painterResource(drawable),
            contentDescription = "avatar",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
            alignment = Alignment.Center,
        )
    }
}


    @Composable
    fun MainGradientBackground() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            skyBabyBlue,
                            cloudPink
                        )
                    )
                )
        ) {
        }
    }

    @Composable
    fun HalfCircleBackground() {
        Surface(
            modifier = Modifier
                .fillMaxHeight(.75f)
                .fillMaxWidth(1f)
                .alpha(.35f),
            shape = HomeScreenShapes.large,
            color = pureWhite,
        ) { }
    }

@Composable
fun HalfCircleBackgroundLonger() {
    Surface(
        modifier = Modifier
            .fillMaxHeight(.85f)
            .fillMaxWidth(1f)
            .alpha(.65f),
        shape = BackgroundShapes.small,
        color = pureWhite,
    ) { }
}

    @Composable
    fun HalfCircleSensorBackground() {
        Surface(
            modifier = Modifier
                .fillMaxHeight(.35f)
                .fillMaxWidth(0.3f)
                .alpha(.30f),
            shape = BackgroundShapes.small,
            color = strawberryRed,
        ) { }
    }



