package com.ui.sensors

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.BaseSensorActivity
import com.androidsensorengine.ui.composables.HalfCircleBackgroundLonger
import com.androidsensorengine.ui.composables.MainGradientBackground
import com.androidsensorengine.ui.composables.SensorCometBackground
import com.androidsensorengine.ui.theme.AndroidSensorEngineTheme
import com.androidsensorengine.utils.Constants
import com.christianfoulcard.android.androidsensorengine.R
import com.ui.composables.CentralLocationGraphicSensorInfo
import com.ui.composables.DisplaySensorTitle
import com.ui.composables.FirstLocationInfoLabelGroup
import com.ui.composables.InfoIcon
import com.ui.composables.PowerButton
import com.ui.composables.SecondLocationInfoLabelGroup
import com.ui.composables.ThirdLocationInfoLabelGroup
import com.ui.sensors.viewmodels.LocationSensorViewModel
import com.utils.PermissionUtils.requestLocationPermission
import com.utils.UIUpdater

class LocationSensor: BaseSensorActivity() {

    private val viewModel: LocationSensorViewModel by viewModels()

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
                    DisplaySensorTitle("Location Sensor")
                    InfoIcon(supportFragmentManager, this@LocationSensor, R.string.light_desc)
                    Column(modifier = Modifier.padding(top = 90.dp)) {
                        CentralLocationGraphicSensorInfo(
                            largeInfoString = "0",
                            superScript = "lux",
                            description = "Brightness",
                            viewModel
                        )
                        FirstLocationInfoLabelGroup("Average Lux", "0", viewModel)
                        SecondLocationInfoLabelGroup("Peak Lux", "0", viewModel)
                        ThirdLocationInfoLabelGroup("Lowest Lux", "0", viewModel)
                    }
                    PowerButton()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        requestLocationPermission(this)
        viewModel.startLocationManager()
        viewModel.requestLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
       // viewModel.startListening()
        UIUpdater().startUpdatingUI(500) { startLiveData() }

    }

    override fun onPause() {
        super.onPause()
    //    viewModel.stopListening()
        UIUpdater().stopUpdatingUI()

    }

    override fun onStop() {
        super.onStop()
        viewModel.removeLocationUpdates()
    }

    private fun startLiveData() {
//        viewModel.averageLocationLiveData.postValue(viewModel.averageLocationReading())
//        viewModel.highestLocationLiveData.postValue(viewModel.highestLocationReading())
//        viewModel.lowestLocationLiveData.postValue(viewModel.lowestLocationReading())
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Location permission must be granted to use this sensor", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

}