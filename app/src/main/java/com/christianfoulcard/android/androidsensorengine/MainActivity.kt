package com.christianfoulcard.android.androidsensorengine

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.Sensors.*
import com.google.firebase.analytics.FirebaseAnalytics

//TODO: Show user a list of sensors their device can use
//TODO: Add elevation/sea level sensor?
//TODO: Fix animations
//TODO Custom Sounds?
//TODO: Notification Logo Size
//TODO: Update ram activity parsing

////////////////////////////////////////////////////////////////////////////////////////////////////
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make sure this theme is before calling super.onCreate
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensor_selection)

        // This will make the Status Bar completely transparent
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Obtain the FirebaseAnalytics instance and Initiate Firebase Analytics
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Intents to start the sensor activities

    fun soundIconIntent(view: View?) {
        val soundIntent = Intent(this, SoundSensorActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.sound_icon)
        val transitionName = getString(R.string.sound_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(soundIntent, transitionActivityOptions.toBundle())
    }

    fun tempIconIntent(view: View?) {
        val tempIntent = Intent(this, TemperatureActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.temp_icon)
        val transitionName = getString(R.string.temp_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(tempIntent, transitionActivityOptions.toBundle())
    }

    fun lightIconIntent(view: View?) {
        val lightIntent = Intent(this, LightSensorActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.light_icon)
        val transitionName = getString(R.string.light_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(lightIntent, transitionActivityOptions.toBundle())
    }

    fun ramIconIntent(view: View?) {
        val ramIntent = Intent(this, RamActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.ram_icon)
        val transitionName = getString(R.string.ram_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(ramIntent, transitionActivityOptions.toBundle())
    }

    fun batteryIconIntent(view: View?) {
        val batteryIntent = Intent(this, BatteryActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.battery_icon)
        val transitionName = getString(R.string.battery_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(batteryIntent, transitionActivityOptions.toBundle())
    }

    fun speedIconIntent(view: View?) {
        val speedIntent = Intent(this, AccelerometerActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.speed_icon)
        val transitionName = getString(R.string.speed_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(speedIntent, transitionActivityOptions.toBundle())
    }

    fun humidityIconIntent(view: View?) {
        val humidityIntent = Intent(this, HumidityActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.humidity_icon)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(humidityIntent, transitionActivityOptions.toBundle())
    }

    fun pressureIconIntent(view: View?) {
        val pressureIntent = Intent(this, PressureActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.pressure_icon)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(pressureIntent, transitionActivityOptions.toBundle())
    }

    fun walkIconIntent(view: View?) {
        val walkIntent = Intent(this, WalkActivity::class.java)
        val sharedView = findViewById<ImageView>(R.id.walk_icon)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, sharedView, transitionName)
        this.startActivity(walkIntent, transitionActivityOptions.toBundle())
    }
}