package com.example.android.soundtechsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//TODO: Make going into other Activities responsive

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_selection);
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     * When tapping the sound icon, this will open the Sound Sensor Activity
     * Go to the sensor_selection.xml to change the onClick values
     */

    public void soundIconIntent(View view) {
        Intent soundIntent = new Intent(this, SoundActivity.class);
       this.startActivity(soundIntent);
       // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }
}