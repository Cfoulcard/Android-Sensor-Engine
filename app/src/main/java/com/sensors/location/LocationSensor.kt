package com.sensors.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.androidsensorengine.utils.LogUtils.TAG
import timber.log.Timber

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
                speedMs = p0.speed.toInt() // This is the standard which returns meters per second.
                speedMph = (p0.speed * 2.2369).toInt() // This is speed in mph
                speedKm = (p0.speed * 3600 / 1000).toInt() // This is speed in km/h
                speedFts = (p0.speed * 3.2808).toInt() // This is speed in Feet per second
                speedKnot = (p0.speed * 1.9438).toInt() // This is speed in knots

                Timber.tag(TAG).d("onLocationChanged: Mph: " + speedMph)
                Timber.tag(TAG).d("onLocationChanged: M/S: " + speedMs)
                Timber.tag(TAG).d("onLocationChanged: KH/H: " + speedKm)
                Timber.tag(TAG).d("onLocationChanged: FT/S: " + speedFts)
                Timber.tag(TAG).d("onLocationChanged: Knots: " + speedKnot)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }
    }

    companion object {
            var speedMs:  Int? = null
            var speedMph: Int?  = null
            var speedKm:  Int? = null
            var speedFts: Int? = null
            var speedKnot:  Int? = null
    }
}