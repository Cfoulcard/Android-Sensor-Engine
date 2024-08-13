package com.ui.sensors

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.BaseSensorActivity
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.androidsensorengine.utils.Constants.AUDIO_PERMISSION_REQUEST_CODE
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.composables.CentralSoundGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstSoundInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondSoundInfoLabelGroup
import com.ui.composables.ThirdSoundInfoLabelGroup
import com.ui.sensors.viewmodels.SoundSensorViewModel
import com.utils.PermissionUtils.requestAudioPermission
import com.utils.UIUpdater

class SoundActivity: BaseSensorActivity() {

    private val viewModel: SoundSensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        .fillMaxSize().verticalScroll(enabled = true, state = ScrollState(initial = -1))
                ) {
                    DisplaySensorTitle("Sound Sensor")
                    InfoIcon(supportFragmentManager, this@SoundActivity, R.string.sound_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralSoundGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "db",
                            description = "Loudness",
                            viewModel,
                        )
                        FirstSoundInfoLabelGroup("Average Decibel Reading", "0", viewModel)
                        SecondSoundInfoLabelGroup("Peak Loudness", "0", viewModel)
                        ThirdSoundInfoLabelGroup("Lowest Decibel", "0", viewModel)
                    }
                    //PowerButton()
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
        UIUpdater().startUpdatingUI(500) { startLiveData() }
    }

    override fun onPause() {
        super.onPause()
        UIUpdater().stopUpdatingUI()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyRecorder()
        viewModel.resetDecibelReading()
    }

    private fun startLiveData() {
        viewModel.decibelLiveData.postValue(viewModel.currentAudioDecibels())
        viewModel.averageDecibelLiveData.postValue(viewModel.averageDecibelReading())
        viewModel.highestDecibelLiveData.postValue(viewModel.highestDecibelReading())
        viewModel.lowestDecibelLiveData.postValue(viewModel.lowestDecibelReading())
    }


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

//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        AndroidSensorEngineTheme {
//            MainGradientBackground()
//            HalfCircleBackgroundLonger()
//            SensorCometBackground()
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                DisplaySensorTitle("Sound Sensor")
//                InfoIcon(supportFragmentManager, this@SoundSensor)
//                Column(modifier = Modifier.padding(top = 90.dp)) {
//                    CentralGraphicSensorInfo(
//                        largeInfoString = "0",
//                        superScript = "db",
//                        description = "Loudness",
//                        viewModel
//                    )
//                    FirstInfoLabelGroup("Average Decibel Reading", "0", viewModel)
//                    SecondInfoLabelGroup("Peak Loudness", "0", viewModel)
//                    ThirdInfoLabelGroup("Lowest Decibel", "0", viewModel)
//                }
//                PowerButton()
//            }
//        }
//    }
}