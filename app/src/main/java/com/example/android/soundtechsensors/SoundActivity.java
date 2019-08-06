package com.example.android.soundtechsensors;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

//TODO if user denies permission recast the prompt upon reopening activity ✔
//TODO Make XML work on any device
//TODO make dB accurate and plug in to the dB meter ✔
//TODO prevent app from crashing if user denies permissions
//TODO create a slide bar to have the user configure how loud the dB should be to make a sound
//TODO Have a list of sounds the user can choose when the dB reaches the user's dB level
//TODO Enable the user to use their own custom media
//TODO provide explanation for the audio request


public class SoundActivity extends AppCompatActivity {

    //Initiates the Media Player to play raw files
    MediaPlayer mp;

    //Creates an instance of the SoundDetector Class. Will be used to access its methods
   // SoundDetector soundDetector = new SoundDetector();

    TextView configuredDecibel;
    TextView currentdb;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    // Initiate Firebase Analystics
    private FirebaseAnalytics mFirebaseAnalytics;

    //Used for record audio permission
    public static final int AUDIO_RECORD_REQUEST_CODE = 122;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 123;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        }
    };

    final Handler mHandler = new Handler();

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_sensor);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Textviews
        configuredDecibel = (TextView) findViewById(R.id.configured_decibel);
        currentdb = (TextView) findViewById(R.id.current_decibel);

        //Seekbar properties
        // https://www.tutlane.com/tutorial/android/android-seekbar-with-examples
        final SeekBar decibelSeekbar= (SeekBar) findViewById(R.id.decibel_seekbar); // initiate the Seekbar
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                // MY_PERMISSIONS_REQUEST_RECORD_AUDIO is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

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
        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(500);
                            // Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }

    //Microphone recording starts
    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    //Stops microphone from recording when user exits activity
    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    //Properties of the microphone
    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
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
        currentdb.setText(Integer.toString((int) soundDb()) + " Current dB");

        //Alternate decibel measurement
        //currentdb.setText(Integer.toString((int) getAmplitudeEMA()) + " Current dB");
    }

    public int soundDb() {
        return (int) (20 * Math.log10(getAmplitudeEMA()));
    }

    //Calculates the decibel valve
    public int getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
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
    private boolean isRecordAudioPermissionGranted()
    {
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
                },AUDIO_RECORD_REQUEST_CODE);
                return false;
            }

        } else {
            // put your code for Version < Marshmallow
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
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
