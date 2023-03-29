package com

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utils.SystemUi

abstract class BaseSensorActivity: AppCompatActivity() {

    //TODO detect network connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUi().hideSystemUIFull(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}