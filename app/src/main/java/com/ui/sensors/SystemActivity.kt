package com.ui.sensors

import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.BaseSensorActivity
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.composables.CentralSystemGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstSystemInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondSystemInfoLabelGroup
import com.ui.sensors.viewmodels.SystemSensorViewModel
import com.utils.LifecycleUtils.startUpdatingUiWithMainCoroutine
import com.utils.UIUpdater
import kotlinx.coroutines.Job

class SystemActivity : BaseSensorActivity() {

    private val viewModel: SystemSensorViewModel by viewModels()
    private var updateJob: Job? = null

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
                        .fillMaxSize().verticalScroll(enabled = true, state = ScrollState(initial = -1))
                ) {
                    DisplaySensorTitle("System Sensor")
                    InfoIcon(supportFragmentManager, this@SystemActivity, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralSystemGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "GB",
                            description = "Available Ram",
                            viewModel
                        )
                        FirstSystemInfoLabelGroup("Total Ram", "0", viewModel)
                        SecondSystemInfoLabelGroup("Threshold", "0", viewModel)
                     //   ThirdSystemInfoLabelGroup("Lowest Lux", "0", viewModel)
                    }
                    PowerButton()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        if (updateJob?.isActive != true) {
            updateJob = lifecycleScope.startUpdatingUiWithMainCoroutine(1000) { viewModel.updateUiWithMemoryInfo() }
        }
        UIUpdater().startUpdatingUI(500) { startLiveData() }
    }

    override fun onPause() {
        super.onPause()
        updateJob?.cancel()
        UIUpdater().stopUpdatingUI()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun startLiveData() {
        viewModel.availableMemoryLiveData.postValue(viewModel.availableMemory)
        viewModel.totalMemoryLiveData.postValue(viewModel.totalMemory)
        viewModel.thresholdMemoryLiveData.postValue(viewModel.thresholdMemory)
    }

}