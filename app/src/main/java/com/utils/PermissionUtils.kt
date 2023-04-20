package com.utils

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import com.androidsensorengine.utils.Constants.AUDIO_PERMISSION_REQUEST_CODE
import com.androidsensorengine.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE

object PermissionUtils {

    fun requestAudioPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            ), AUDIO_PERMISSION_REQUEST_CODE)
    }

    fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE)
    }


}