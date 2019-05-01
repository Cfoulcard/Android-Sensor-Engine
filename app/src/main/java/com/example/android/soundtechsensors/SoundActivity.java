package com.example.android.soundtechsensors;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

public class SoundActivity extends AppCompatActivity {

    //Initiates the Media Player to play raw files
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_sensor);
        mp = MediaPlayer.create(this, R.raw.lightningsoundtest);
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    //Sound Test
    public void playSound(View v) {
        mp.start();
    }

}
