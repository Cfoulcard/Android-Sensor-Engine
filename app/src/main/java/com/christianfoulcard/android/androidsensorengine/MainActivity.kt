package com.christianfoulcard.android.androidsensorengine

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.BuildConfig.DEBUG
import com.christianfoulcard.android.androidsensorengine.Sensors.*
import com.christianfoulcard.android.androidsensorengine.databinding.SensorSelectionBinding
import com.christianfoulcard.android.androidsensorengine.databinding.SoundSensorBinding
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import timber.log.Timber.DebugTree

//TODO: Show user a list of sensors their device can use
//TODO: Add elevation/sea level sensor?
//TODO: Fix animations
//TODO: Update ram activity parsing



////////////////////////////////////////////////////////////////////////////////////////////////////
class MainActivity : AppCompatActivity() {

    //View Binding to call the layout's views
    private lateinit var binding: SensorSelectionBinding

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make sure this theme is before calling super.onCreate
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = SensorSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // This will make the Status Bar completely transparent
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

//        if (BuildConfig.DEBUG) {
//            Timber.plant(DebugTree())
//        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Intents to start the sensor activities

    fun soundIconIntent(view: View?) {
        val soundIntent = Intent(this, SoundSensorActivity::class.java)
        val transitionName = getString(R.string.sound_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.soundIcon, transitionName)
        this.startActivity(soundIntent, transitionActivityOptions.toBundle())
    }

    fun tempIconIntent(view: View?) {
        val tempIntent = Intent(this, TemperatureActivity::class.java)
        val transitionName = getString(R.string.temp_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.tempIcon, transitionName)
        this.startActivity(tempIntent, transitionActivityOptions.toBundle())
    }

    fun lightIconIntent(view: View?) {
        val lightIntent = Intent(this, LightSensorActivity::class.java)
        val transitionName = getString(R.string.light_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.lightIcon, transitionName)
        this.startActivity(lightIntent, transitionActivityOptions.toBundle())
    }

    fun ramIconIntent(view: View?) {
        val ramIntent = Intent(this, RamActivity::class.java)
        val transitionName = getString(R.string.ram_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.ramIcon, transitionName)
        this.startActivity(ramIntent, transitionActivityOptions.toBundle())
    }

    fun batteryIconIntent(view: View?) {
        val batteryIntent = Intent(this, BatteryActivity::class.java)
        val transitionName = getString(R.string.battery_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.batteryIcon, transitionName)
        this.startActivity(batteryIntent, transitionActivityOptions.toBundle())
    }

    fun speedIconIntent(view: View?) {
        val speedIntent = Intent(this, AccelerometerActivity::class.java)
        val transitionName = getString(R.string.speed_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.speedIcon, transitionName)
        this.startActivity(speedIntent, transitionActivityOptions.toBundle())
    }

    infix fun humidityIconIntent(view: View?) {
        val humidityIntent = Intent(this, HumidityActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.humidityIcon, transitionName)
        this.startActivity(humidityIntent, transitionActivityOptions.toBundle())
    }

    fun pressureIconIntent(view: View?) {
        val pressureIntent = Intent(this, PressureActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.pressureIcon, transitionName)
        this.startActivity(pressureIntent, transitionActivityOptions.toBundle())
    }

    fun walkIconIntent(view: View?) {
        val walkIntent = Intent(this, WalkActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.walkIcon, transitionName)
        this.startActivity(walkIntent, transitionActivityOptions.toBundle())
    }
}


