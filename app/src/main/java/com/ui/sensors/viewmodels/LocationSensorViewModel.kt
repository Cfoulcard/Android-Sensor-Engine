package com.ui.sensors.viewmodels

import android.location.LocationListener
import androidx.lifecycle.ViewModel
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.sensors.location.LocationSensor

class LocationSensorViewModel: ViewModel() {

    private val locationSensor = LocationSensor(globalAppContext)
    private var locationListener: LocationListener? = null

    fun startLocationManager() {
        locationListener = locationSensor.createLocationListener()
        locationSensor.requestLocationUpdates(locationListener!!)
    }

    fun requestLocationUpdates() {
        locationSensor.requestLocationUpdates(locationListener!!)
    }

    fun removeLocationUpdates() {
        locationListener?.let { locationSensor.removeLocationManager(it) }
        locationListener = null
    }

}

