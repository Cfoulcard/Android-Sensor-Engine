package com.androidsensorengine.ui.composables

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidsensorengine.ui.theme.*
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.sensors.*

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
        onClick = { navigateToSoundSensor(context) }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LightIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToLightSensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(12.dp),
            alignment = Alignment.Center,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PressureIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToPressureSensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(12.dp),
            alignment = Alignment.Center,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AmbientTemperatureIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToAmbientTemperatureSensor(context) }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BatteryIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToBatterySensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(16.dp),
            alignment = Alignment.Center,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SystemIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToSystemSensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(8.dp),
            alignment = Alignment.Center,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HumidityIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToHumiditySensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(16.dp),
            alignment = Alignment.Center,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationIcon(context: Context, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        backgroundColor = pureWhite,
        shape = HomeScreenShapes.small,
        elevation = 12.dp,
        onClick = { navigateToLocationSensor(context) }
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "sensorIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(16.dp).fillMaxSize().padding(12.dp),
            alignment = Alignment.Center,

            )
    }
}

fun navigateToSoundSensor(context: Context) {
    context.startActivity(Intent(context, SoundActivity::class.java))
    }

fun navigateToLightSensor(context: Context) {
    context.startActivity(Intent(context, LightSensor::class.java))
}

fun navigateToPressureSensor(context: Context) {
    context.startActivity(Intent(context, PressureSensor::class.java))
}

fun navigateToAmbientTemperatureSensor(context: Context) {
    context.startActivity(Intent(context, AmbientTemperatureActivity::class.java))
}

fun navigateToBatterySensor(context: Context) {
    context.startActivity(Intent(context, BatteryActivity::class.java))
}

fun navigateToSystemSensor(context: Context) {
    context.startActivity(Intent(context, SystemActivity::class.java))
}

fun navigateToHumiditySensor(context: Context) {
    context.startActivity(Intent(context, HumidityActivity::class.java))
}

fun navigateToLocationSensor(context: Context) {
    context.startActivity(Intent(context, LocationActivity::class.java))
}

@Composable
fun SensorCometBackground() {
    Image(
        painter = painterResource(R.drawable.ic_comet_shadow),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxHeight(0.43f).fillMaxWidth(1f).fillMaxSize().blur(8.dp).alpha(1f),
        alignment = Alignment.Center,
    )
    Image(
        painter = painterResource(R.drawable.ic_comet_gradient),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxHeight(0.4f).fillMaxWidth(1f).fillMaxSize(),
        alignment = Alignment.Center,
        )
}

@Composable
fun SensorCometBackground2() {
    Image(
        painter = painterResource(R.drawable.ic_comet_circle),
        contentDescription = "cometGradient",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth(1f).fillMaxSize(),
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
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(4.dp).weight(.85f),
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
fun SensorDiagnosisStatusRow(text: String, condition: Boolean) {
    val drawable = if (condition) R.drawable.green_circle else R.drawable.red_circle
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.6f),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(4.dp).weight(.85f),
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
            .alpha(.50f),
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



