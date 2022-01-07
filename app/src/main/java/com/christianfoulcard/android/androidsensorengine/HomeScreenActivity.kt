package com.christianfoulcard.android.androidsensorengine

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.sensors.SensorSoundActivity
import com.christianfoulcard.android.androidsensorengine.sensors.*
import com.christianfoulcard.android.androidsensorengine.databinding.ActivitySensorSelectionBinding

// TODO: Show user a list of sensors their device can use
// TODO: Add elevation/sea level sensor?
// TODO: Fix animations
// TODO: Update ram activity parsing

class HomeScreenActivity : AppCompatActivity() {

    // View Binding to call the layout's views
    private lateinit var binding: ActivitySensorSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this theme is before calling super.onCreate
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivitySensorSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // This will make the Status Bar completely transparent
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onStart() {
        super.onStart()
    }

    // Intents to start the sensor activities. These are called in the sensor selection XML file

    fun soundIconIntent(view: View?) {
        val soundIntent = Intent(this, SensorSoundActivity::class.java)
        val transitionName = getString(R.string.sound_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.soundIcon, transitionName)
        this.startActivity(soundIntent, transitionActivityOptions.toBundle())
    }

    fun tempIconIntent(view: View?) {
        val tempIntent = Intent(this, SensorTemperatureActivity::class.java)
        val transitionName = getString(R.string.temp_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.tempIcon, transitionName)
        this.startActivity(tempIntent, transitionActivityOptions.toBundle())
    }

    fun lightIconIntent(view: View?) {
        val lightIntent = Intent(this, SensorLightActivity::class.java)
        val transitionName = getString(R.string.light_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.lightIcon, transitionName)
        this.startActivity(lightIntent, transitionActivityOptions.toBundle())
    }

    fun ramIconIntent(view: View?) {
        val ramIntent = Intent(this, SensorRamActivity::class.java)
        val transitionName = getString(R.string.ram_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.ramIcon, transitionName)
        this.startActivity(ramIntent, transitionActivityOptions.toBundle())
    }

    fun batteryIconIntent(view: View?) {
        val batteryIntent = Intent(this, SensorBatteryActivity::class.java)
        val transitionName = getString(R.string.battery_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.batteryIcon, transitionName)
        this.startActivity(batteryIntent, transitionActivityOptions.toBundle())
    }

    fun speedIconIntent(view: View?) {
        val speedIntent = Intent(this, SensorAccelerometerActivity::class.java)
        val transitionName = getString(R.string.speed_anim)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.speedIcon, transitionName)
        this.startActivity(speedIntent, transitionActivityOptions.toBundle())
    }

    fun humidityIconIntent(view: View?) {
        val humidityIntent = Intent(this, SensorHumidityActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.humidityIcon, transitionName)
        this.startActivity(humidityIntent, transitionActivityOptions.toBundle())
    }

    fun pressureIconIntent(view: View?) {
        val pressureIntent = Intent(this, SensorPressureActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.pressureIcon, transitionName)
        this.startActivity(pressureIntent, transitionActivityOptions.toBundle())
    }

    fun walkIconIntent(view: View?) {
        val walkIntent = Intent(this, SensorWalkActivity::class.java)
        val transitionName = ""
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this@HomeScreenActivity, binding.walkIcon, transitionName)
        this.startActivity(walkIntent, transitionActivityOptions.toBundle())
    }
}


