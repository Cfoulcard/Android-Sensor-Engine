package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.christianfoulcard.android.androidsensorengine.R.layout.ram_sensor;

public class RamActivity extends AppCompatActivity {

    //Dialog popup info
    Dialog ramInfoDialog;

    //TextViews
    TextView ramText;
    TextView currentRam;
    TextView ramSensor;

    //ImsgeViews
    ImageView ramInfo;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(ram_sensor);

        //TextViews
        ramText = findViewById(R.id.ram);
        currentRam = findViewById(R.id.current_ram);
        ramSensor = findViewById(R.id.ram_sensor);

        //ImageViews
        ramInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        ramInfoDialog = new Dialog(this);

        //Animation for TextView fade in
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        ramText.startAnimation(in);
        currentRam.startAnimation(in);
        ramSensor.startAnimation(in);
        ramInfo.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        currentRam = findViewById(R.id.current_ram);


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        long i = (getMemorySize());
        currentRam.setText((i) + " mB");
        super.onResume();
    }

    public int getMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB =(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;
//TODO constantly update memory text
        return (int) availableMegs;
    }

    private void unbindService(String activityService) {
    }

    //Unbind ACTIVITY_SERVICE to release ram resources
    public void onDestroy() {
        super.onDestroy();

        unbindService(ACTIVITY_SERVICE);
    }

    public void showRamDialogPopup(View v) {
        ramInfoDialog.setContentView(R.layout.ram_popup_info);

        ramInfoDialog.show();
    }

    public void closeRamDialogPopup(View v) {
        ramInfoDialog.setContentView(R.layout.ram_popup_info);

        ramInfoDialog.dismiss();
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    //The following is for the menu items within the navigation_menu.xml file
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent configurationsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(configurationsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}