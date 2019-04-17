package com.example.android.soundtechsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnContextClickListener;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_selection);
    }

    /**
     * When tapping the sound icon, this will open the Sound Sensor Activity
     */

    /**
    ImageView soundIcon = (ImageView) findViewById(R.id.soundIcon);
    soundIcon.OnContextClickListener(new View.OnContextClickListener() {

        public void onClick(View view) {
        Intent soundIntent = new Intent(this, SoundActivity.class);
        startActivity(soundIntent);
    }
    }
     */
}