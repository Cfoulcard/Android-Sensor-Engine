package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.christianfoulcard.android.androidsensorengine.Menu_Items.About;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Credits;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Premium;
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

////////////////////////////////////////////////////////////////////////////////////////////////////

public class SoundSensorActivity extends AppCompatActivity {

    //Initiates the Media Player to play raw files
    MediaPlayer mp;

    //Dialog popup info
    Dialog soundInfoDialog;

    //TextView Data
    TextView configuredDecibel;
    TextView decibels;
    TextView soundSensor;
    TextView currentdb;

    //Image Views
    ImageView soundInfo;
    ImageView soundLogo;

    //For sound recording + converting to sound data
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    final Runnable updater = new Runnable() {

        public void run() {
            updateTv();
        }
    };

    final Handler mHandler = new Handler();

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    //Used for record audio permission
    public static final int AUDIO_RECORD_REQUEST_CODE = 122;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        //Enable for fade in transition
        // overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.sound_sensor);


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //TextViews
        configuredDecibel = (TextView) findViewById(R.id.current_decibel);
        decibels = (TextView) findViewById(R.id.decibels);
        soundSensor = (TextView) findViewById(R.id.sound_sensor);

        //ImageViews
        soundInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Sound Info
        soundInfoDialog = new Dialog(this);

        //Animation that plays when entering/exiting Activity
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        configuredDecibel.startAnimation(in);
        decibels.startAnimation(in);
        soundSensor.startAnimation(in);
        soundInfo.startAnimation(in);

        //To request audio permissions upon opening activity
        requestAudioPermissions();

        //Seekbar properties
        // https://www.tutlane.com/tutorial/android/android-seekbar-with-examples
        final SeekBar decibelSeekbar = (SeekBar) findViewById(R.id.decibel_seekbar); // initiate the Seekbar
        //  decibelSeekbar.setMax(999); // 999 maximum value for the Seek bar
        //decibelSeekbar.getProgress(); // 50 default progress value
        // configuredDecibel.setText(decibelSeekbar.getProgress() + "/" + decibelSeekbar.getMax());

        //This variable will initiate and create the MediaPlayer
        mp = MediaPlayer.create(this, R.raw.lightningsoundtest);

        //Decibel Seekbar configuration
        decibelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
                configuredDecibel.setText(decibelSeekbar.getProgress() + "/" + decibelSeekbar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                configuredDecibel.setText(decibelSeekbar.getProgress() + "/" + decibelSeekbar.getMax());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                configuredDecibel.setText(decibelSeekbar.getProgress() + "/" + decibelSeekbar.getMax());
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // This section is used to request permission to utilize the user's microphone and record audio
        // Refer to https://developer.android.com/training/permissions/requesting.html#java


        // Used to test request audio recording permission as a toast message
 /*       if(!isRecordAudioPermissionGranted())
        {
            Toast.makeText(getApplicationContext(), "Need to request permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No need to request permission", Toast.LENGTH_SHORT).show();
        }*/


        ////////////////////////////////////////////////////////////////////////////////////////////////
        //This section is used to pick up sound from the user's microphone

        //Adjust milliseconds to change refresh rate of decibel tracker
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null) {
                        try {
                            Thread.sleep(500);
                            // Log.i("Noise", "Tock");
                        } catch (InterruptedException e) {
                        }
                        ;
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Toast.makeText(this, "Please grant permission to measure sound", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(this, R.string.sound_permission_denied, Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Microphone recording starts
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startRecorder();
    }



    //Stops microphone from recording when user exits activity
    public void onPause() {
        super.onPause();
        stopRecorder();
    }

    //Properties of the microphone
    public void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }



 /*   public void setConfiguredDecibel() {
        configuredDecibel.setText(onProgressChanged());
    }*/

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //This section controls the decibel properties picked up by the microphone
    //The formula attempts to emulate SPL (Sound Pressure Level)
    //Read up more at https://www.wikiwand.com/en/Sound_pressure
    //For more decibel detail change Integer to Double
    public void updateTv() {


        if (soundDb() > 0) {
            configuredDecibel.setText(Integer.toString((int) soundDb()) + " dB");
        }

/*        //Change color of decibels based upon loudness
        if (soundDb() > 70) {
            configuredDecibel.setTextColor(Color.RED);
        } else if (soundDb() < 69 && soundDb() > 30) {
            configuredDecibel.setTextColor(Color.WHITE);
        } else if (soundDb() < 30) {
            configuredDecibel.setTextColor(Color.WHITE);
        }*/

        //Alternate decibel measurement
        //configuredDecibel.setText(Integer.toString((int) getAmplitudeEMA()) + " Current dB");
    }

    public int soundDb() {
        return (int) (20 * Math.log10(getAmplitudeEMA()));
    }

    //Calculates the decibel valve
    public int getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public int getAmplitudeEMA() {
        int amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1 - EMA_FILTER) * mEMA;
        return (int) mEMA;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Record Audio Permission
    //Upon opening this activity user will be promoted to allow audio recording
    //TODO Update Build Version
    private boolean isRecordAudioPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
                return true;
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO
                }, AUDIO_RECORD_REQUEST_CODE);
                return false;
            }

        } else {
            // put your code for Version < Marshmallow
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void showSoundDialogPopup(View v) {
        soundInfoDialog.setContentView(R.layout.sound_popup_info);

        soundInfoDialog.show();
    }

    public void closeSoundDialogPopup(View v) {
        soundInfoDialog.setContentView(R.layout.sound_popup_info);

        soundInfoDialog.dismiss();
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

    //Sound Test provided by MediaPlayer
    public void playSound(View v) {
        mp.start();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

// Helpful Resources

    //Measure dB
    //https://stackoverflow.com/questions/15693990/measuring-decibels-with-mobile-phone


}