package com.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.androidsensorengine.ui.composables.SensorDiagnosisStatusRow
import com.androidsensorengine.ui.composables.SensorIcon
import com.androidsensorengine.ui.composables.SystemIcon
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.androidsensorengine.ui.theme.HomeScreenShapes
import com.androidsensorengine.ui.theme.pureWhite
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.sensors.viewmodels.HomeScreenViewModel
import com.utils.SystemUi

class HomeScreenActivity : ComponentActivity() {

    private val viewModel: HomeScreenViewModel by viewModels()

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
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreenSensorGrid()
                    SensorDiagnosisView()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNewPreferencesValues()
    }

    @Composable
    fun HomeScreenSensorGrid() {

        val data by remember { mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7", "8")) }

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
                        LightIcon(this@HomeScreenActivity, R.drawable.light_lightbulb_icon)
                    }

                    "3" -> {
                        PressureIcon(this@HomeScreenActivity, R.drawable.pressure_gauge_meter_icon)
                    }

                    "4" -> {
                        AmbientTemperatureIcon(this@HomeScreenActivity, R.drawable.temperature_icon)
                    }

                    "5" -> {
                        BatteryIcon(this@HomeScreenActivity, R.drawable.battery_charging_sign_icon)
                    }

                    "6" -> {
                        SystemIcon(this@HomeScreenActivity, R.drawable.semiconductor_icon)
                    }

                    "7" -> {
                        HumidityIcon(this@HomeScreenActivity, R.drawable.water_drop_teardrop_icon)
                    }

                    "8" -> {
                        LocationIcon(this@HomeScreenActivity, R.drawable.gps_icon)
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
        val soundCondition by viewModel.soundCondition.observeAsState(false)
        val atmosphereCondition by viewModel.atmosphereCondition.observeAsState(false)
        val moistHumidCondition by viewModel.moistHumidCondition.observeAsState(false)
        val gpsCondition by viewModel.gpsCondition.observeAsState(false)
        val lightCondition by viewModel.lightCondition.observeAsState(false)
        val systemCondition by viewModel.systemCondition.observeAsState(false)
        val batteryCondition by viewModel.batteryCondition.observeAsState(false)
        val ambientCondition by viewModel.ambientCondition.observeAsState(false)

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
                    .verticalScroll(enabled = true, state = ScrollState(initial = -1))
            ) {
                Text(
                    text = "Sensor Diagnosis",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h1
                )

                SensorDiagnosisStatusRow("Sound and Audio", soundCondition)
                SensorDiagnosisStatusRow("Atmospheric Pressure", atmosphereCondition)
                SensorDiagnosisStatusRow("Moisture and Humidity", moistHumidCondition)
                SensorDiagnosisStatusRow("Accelerometer", gpsCondition)
                SensorDiagnosisStatusRow("Light Sensor", lightCondition)
                SensorDiagnosisStatusRow("Ram Sensor", systemCondition)
                SensorDiagnosisStatusRow("Battery Temperature", batteryCondition)
                SensorDiagnosisStatusRow("Ambient Temperature", ambientCondition)
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
                modifier = Modifier.padding(0.dp)
            ) {
                HomeScreenSensorGrid()
                SensorDiagnosisView()
            }
        }
    }
}