package com.example.android.soundtechsensors;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryActivity extends AppCompatActivity {

   // public Context context;
    TextView currentBattery;
    private Context context;
    private int mBatteryLevel;
    private IntentFilter ifilter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_sensor);

        currentBattery = findViewById(R.id.current_battery);
        int i = registerMyReceiver();
        currentBattery.setText((i) + " percent");
    }

    /*public int getBatteryPct(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

      //  int level = batteryStatus.getIntExtra(EXTRA_LEVEL, -1);
     //   int scale = batteryStatus.getIntExtra(EXTRA_SCALE, -1);
        assert batteryStatus != null;
        String temp = batteryStatus.getStringExtra(EXTRA_TEMPERATURE);

       // return (int) (level / (float)scale);
        return Integer.parseInt(temp);
    }*/

/*    BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            Toast.makeText(context, "Battery" + mBatteryLevel, Toast.LENGTH_LONG).show();

        }
    };*/

/*    private String registerMyReceiver() {
        mBatteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, mBatteryLevelFilter);
        return new String(String.valueOf(mBatteryLevel));
    }*/

    BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int percent = (level * 100) / scale;
            float batteryPct = level / (float)scale;

           // return batteryPct;

            Toast.makeText(context, "Current Battery Level: " + level, Toast.LENGTH_LONG).show();
        }
    };

    private int registerMyReceiver() {
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, ifilter);
       // return (BatteryManager.EXTRA_LEVEL);

       // return mBatteryLevel;
        return 0;
    }
}
