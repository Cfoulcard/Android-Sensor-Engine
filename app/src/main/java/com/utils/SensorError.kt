package com.utils

import android.content.Context
import android.widget.Toast

class SensorError {

    fun showNoSensorToast(context: Context) {
        Toast.makeText(context, "Your device does not support this sensor", Toast.LENGTH_SHORT).show()
    }
}