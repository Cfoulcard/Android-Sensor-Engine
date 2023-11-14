package com.androidsensorengine.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidsensorengine.ui.composables.AmbientTemperatureIcon
import com.androidsensorengine.ui.composables.BatteryIcon
import com.androidsensorengine.ui.composables.HalfCircleBackground
import com.androidsensorengine.ui.composables.HumidityIcon
import com.androidsensorengine.ui.composables.LightIcon
import com.androidsensorengine.ui.composables.LocationIcon
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.PressureIcon
import com.androidsensorengine.ui.composables.SensorDiagnosisRow
import com.androidsensorengine.ui.composables.SensorIcon
import com.androidsensorengine.ui.composables.SystemIcon
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.androidsensorengine.utils.LogUtils.TAG
import com.christianfoulcard.android.androidsensorengine.R
import com.utils.SystemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.androidsensorengine.ui.theme.HomeScreenShapes
import com.androidsensorengine.ui.theme.pureWhite

class HomeScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUi().hideSystemUIFull(this)
        setContent {
            AndroidSensorEngineTheme {
                MainGradientBackground()
                HalfCircleBackground()
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HomeScreenSensorGrid()
                    SensorDiagnosisView()

                }
            }
        }
    }


    @Composable
    fun HomeScreenSensorGrid() {

        val data by remember { mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7", "8",)) }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier.padding(vertical = 72.dp, horizontal = 36.dp),
        ) {

            items(data) { item ->
                when (item) {
                    "1" -> {
                        SensorIcon(this@HomeScreenActivity, R.drawable.ic_sound_wave)
                    }
                    "2" -> {
                        LightIcon(this@HomeScreenActivity, R.drawable.light_bulb_nature_icon)
                    }
                    "3" -> {
                        PressureIcon(this@HomeScreenActivity, R.drawable.ic_barometer_icon_2)
                    }
                    "4" -> {
                        AmbientTemperatureIcon(this@HomeScreenActivity, R.drawable.temperature_icon)
                    }
                    "5" -> {
                        BatteryIcon(this@HomeScreenActivity, R.drawable.ic_battery_icon_2)
                    }
                    "6" -> {
                        SystemIcon(this@HomeScreenActivity, R.drawable.ic_ram_icon_2)
                    }
                    "7" -> {
                        HumidityIcon(this@HomeScreenActivity, R.drawable.ic_humidity_icon_2)
                    }
                    "8" -> {
                        LocationIcon(this@HomeScreenActivity, R.drawable.ic_speed_icon_2)
                    }
                    else -> {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(100.dp),
                        backgroundColor = pureWhite,
                        shape = HomeScreenShapes.small,
                        elevation = 12.dp,
                    ) {
                        Text(
                            text = item,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(32.dp),
                            style = MaterialTheme.typography.h1
                        )
                    }
                }
                }

            }
        }
    }

    @Composable
    fun SensorDiagnosisView() {
        Surface(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxSize(),
            shape = HomeScreenShapes.medium,
            color = pureWhite,
            elevation = 12.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp)
                    .verticalScroll(enabled = true, state = ScrollState(initial = -1)))
            {
                Text(
                    text = "Sensor Diagnosis",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h1
                )
                SensorDiagnosisRow("Sound and Audio", R.drawable.green_circle)
                SensorDiagnosisRow("Atmospheric Pressure", R.drawable.red_circle)
                SensorDiagnosisRow("Moisture and Humidity", R.drawable.green_circle)
                SensorDiagnosisRow("Accelerometer", R.drawable.green_circle)
                SensorDiagnosisRow("Light Sensor", R.drawable.red_circle)
                SensorDiagnosisRow("Ram Sensor", R.drawable.green_circle)
                SensorDiagnosisRow("Battery Temperature", R.drawable.red_circle)
                SensorDiagnosisRow("Ambient Temperature", R.drawable.green_circle)


            }

        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AndroidSensorEngineTheme {
            MainGradientBackground()
            HalfCircleBackground()
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp)
            ) {
                HomeScreenSensorGrid()
                SensorDiagnosisView()
            }
        }
    }
}