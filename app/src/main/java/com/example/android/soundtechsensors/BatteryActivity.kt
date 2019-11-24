package com.example.android.soundtechsensors

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.battery_sensor.*

class BatteryActivity : AppCompatActivity() {

    internal lateinit var battery_text: TextView
    internal lateinit var currentBattery: TextView
    private val context: Context? = null
    private val mBatteryLevel: Int = 0
    private var ifilter: IntentFilter? = null
   // private val level = registerMyReceiver()

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //Initiate Broadcast Receiver to utilize BatteryManager Features
    //Is parsed by registerMyReceiver() to the textview
    private var mBatteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val percent = level * 100 / scale
            val batteryPct = level / scale.toFloat()

            currentBattery.text = "$level"

           // Toast.makeText(context, "Current Battery Level: $level", Toast.LENGTH_LONG).show()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_sensor)

        battery_text = findViewById(R.id.battery) as TextView
        currentBattery = findViewById(R.id.current_battery) as TextView

        val `in` = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        battery_text.startAnimation(`in`)
        currentBattery.startAnimation(`in`)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        currentBattery = findViewById(R.id.current_battery)
        currentBattery.text = "${registerMyReceiver()} percent"

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun registerMyReceiver(): BroadcastReceiver {
        ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBatteryReceiver, ifilter)

        return mBatteryReceiver
    }

    //This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }
}