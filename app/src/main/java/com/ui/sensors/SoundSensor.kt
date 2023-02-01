package com.androidsensorengine.ui.sensors

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.androidsensorengine.utils.Constants.AUDIO_PERMISSION_REQUEST_CODE
import com.ui.composables.*
import com.ui.sensors.viewmodels.SoundSensorViewModel
import com.utils.PermissionUtils.requestAudioPermission
import com.utils.SystemUi
import kotlinx.coroutines.*

class SoundSensor: ComponentActivity() {

    private val viewModel: SoundSensorViewModel by viewModels()
    private var uiUpdaterJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUi().hideSystemUIFull(this)
        viewModel.createRecorder(this)

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
                            largeInfoString = "0",
                            superScript = "db",
                            description = "Loudness",
                            viewModel
                        )
                        FirstInfoLabelGroup("Average Decibel Reading", "0", viewModel)
                        SecondInfoLabelGroup("Peak Loudness", "0", viewModel)
                        ThirdInfoLabelGroup("Lowest Decibel", "0", viewModel)
                    }
                    PowerButton()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestAudioPermission(this)
        viewModel.startRecorder()
    }

    override fun onResume() {
        super.onResume()
        viewModel.measureDecibels(this)
        updateUI(500)
    }

    override fun onPause() {
        super.onPause()
        stopUpdatingUI()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyRecorder()
        viewModel.resetDecibelReading()
    }

    private fun updateUI(updateMilli: Long) {
        uiUpdaterJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(updateMilli)
                viewModel.decibelLiveData.postValue(viewModel.currentAudioDecibels())
                viewModel.averageDecibelLiveData.postValue(viewModel.averageDecibelReading())
                viewModel.highestDecibelLiveData.postValue(viewModel.highestDecibelReading())
                viewModel.lowestDecibelLiveData.postValue(viewModel.lowestDecibelReading())
            }
        }
    }

    private fun stopUpdatingUI() { uiUpdaterJob?.cancel() }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AUDIO_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Audio permission must be granted to use this sensor", Toast.LENGTH_LONG).show()
                }
                return
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
                        largeInfoString = "0",
                        superScript = "db",
                        description = "Loudness",
                        viewModel
                    )
                    FirstInfoLabelGroup("Average Decibel Reading", "67", viewModel)
                    SecondInfoLabelGroup("Peak Loudness", "85", viewModel)
                    ThirdInfoLabelGroup("Lowest Decibel", "27", viewModel)
                }
                PowerButton()
            }
        }
    }
}