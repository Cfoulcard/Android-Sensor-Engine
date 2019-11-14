package com.example.android.soundtechsensors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;

// TODO Change value of light
//TODO add menu

public class LightActivity extends AppCompatActivity {


    LightSensorActivity lightSensorActivity = new LightSensorActivity();


    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.lux_sensor);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Textviews

     //   lightSensorActivity.onResume();




    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

}
