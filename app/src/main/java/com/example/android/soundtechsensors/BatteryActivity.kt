package com.example.android.soundtechsensors

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.battery_sensor.*

class BatteryActivity : AppCompatActivity() {

    // public Context context;
    internal lateinit var currentBattery: TextView
    private val context: Context? = null
    private val mBatteryLevel: Int = 0
    private var ifilter: IntentFilter? = null
   // private val level = registerMyReceiver()


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


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_sensor)

        currentBattery = findViewById(R.id.current_battery)
        currentBattery.text = "${registerMyReceiver()} percent"

    }


    private fun registerMyReceiver(): BroadcastReceiver {
        ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBatteryReceiver, ifilter)

        return mBatteryReceiver
    }
}