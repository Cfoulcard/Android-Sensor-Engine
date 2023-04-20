package com.sensors.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.androidsensorengine.utils.LogUtils.TAG

class LocationSensor(val context: Context) {

    private var locationManager: LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @RequiresApi(Build.VERSION_CODES.P)
    fun isLocationEnabled() : Boolean? {
        return locationManager?.isLocationEnabled
    }

    fun requestLocationUpdates(listener: LocationListener) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            listener
        )
    }

    fun removeLocationManager(listener: LocationListener) {
        locationManager?.removeUpdates(listener)
        locationManager = null
    }

    fun createLocationListener(): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                Log.d(TAG, "onLocationChanged: ${((p0.speed * 3.2808).toInt())}")
                speed = p0.speed.toInt()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }
    }

    companion object {
        var speed: Int? = null
    }
}