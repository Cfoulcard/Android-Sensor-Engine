package com.androidsensorengine.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

class LocationSensor : LocationListener {

    fun getLocationManager(context: Context) : LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun isLocationEnabled(context: Context) : Boolean {
        return getLocationManager(context).isLocationEnabled
    }

    fun requestLocationUpdates(context: Context) {
        getLocationManager(context).requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            this
        )
    }

    private fun removeLocationManager(context: Context) {
        getLocationManager(context).removeUpdates(this)
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }
}