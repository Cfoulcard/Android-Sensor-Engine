package com.christianfoulcard.android.androidsensorengine

import android.app.ActivityOptions
import android.content.Intent
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.ImageView
import com.christianfoulcard.android.androidsensorengine.Sensors.SoundSensorActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
// @SmallTest
        public class TestMainActivity {

           private var mainActivity: MainActivity? = null

    @Before
    open fun setUp() { mainActivity = MainActivity() }

    @Test
    open fun intentTracker() {
        mainActivity?.humidityIconIntent(view = null)
    }



        }