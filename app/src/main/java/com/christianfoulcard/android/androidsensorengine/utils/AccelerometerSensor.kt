package com.christianfoulcard.android.androidsensorengine.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log

class AccelerometerSensor(val context: Context) : LocationListener {

    private val TAG = AccelerometerSensor::class.simpleName
    private var locationManager: LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun getLocationManager() : LocationManager? {
        return locationManager
    }

    fun isLocationEnabled() : Boolean? {
        return locationManager?.isLocationEnabled
    }

    fun requestLocationUpdates() {
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            this
        )
    }

    fun removeLocationManager() {
        locationManager?.removeUpdates(this)
        locationManager = null
    }

    override fun onLocationChanged(p0: Location) {
        Log.d(TAG, "onLocationChanged: ${((p0.speed * 3.2808).toInt())}")
        speed = p0.speed.toInt()
    }

    companion object {
        var speed: Int? = null
    }
}