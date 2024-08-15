package com.ui.sensors.viewmodels

import android.location.LocationListener
import android.telephony.CarrierConfigManager.Gps
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidsensorengine.utils.Constants.GPS_PREFS
import com.application.AndroidSensorEngine.Companion.globalAppContext
import com.preferences.AppSharedPrefs
import com.sensors.location.LocationSensor
import com.sensors.location.LocationSensor.Companion.speedMph

class LocationSensorViewModel: ViewModel() {

    private val locationSensor = LocationSensor(globalAppContext)
    private var locationListener: LocationListener? = null

    val speedDataLiveValue = MutableLiveData<Int>(0)

    fun startLocationManager() {
        locationListener = locationSensor.createLocationListener()
        locationSensor.requestLocationUpdates(locationListener!!)
    }

    fun requestLocationUpdates() {
        locationSensor.requestLocationUpdates(locationListener!!)
        AppSharedPrefs().saveCondition(GPS_PREFS, true)
    }

    fun getCurrentSpeed() : Int {
        return speedMph?: 0
    }


    fun removeLocationUpdates() {
        locationListener?.let { locationSensor.removeLocationManager(it) }
        locationListener = null
    }

}

