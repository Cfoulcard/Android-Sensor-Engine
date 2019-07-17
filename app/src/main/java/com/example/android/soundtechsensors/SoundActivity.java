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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;


public class SoundActivity extends AppCompatActivity {

    //Initiates the Media Player to play raw files
    MediaPlayer mp;

    //Creates an instance of the SoundDetector Class. Will be used to access its methods
   // SoundDetector soundDetector = new SoundDetector();

    TextView currentdb;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    private FirebaseAnalytics mFirebaseAnalytics;

    //Used for record audio permission
    public static final int AUDIO_RECORD_REQUEST_CODE = 122;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 123;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };
    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_sensor);

        currentdb = (TextView) findViewById(R.id.current_decibel);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //This variable will initiate and create the MediaPlayer
        mp = MediaPlayer.create(this, R.raw.lightningsoundtest);


        //TODO if user denies permission recast the prompt upon reopening activity ✔
        //TODO Make XML work on any device
        //TODO make dB accurate and plug in to the dB meter
        //TODO prevent app from crashing if user deny's permissions and clicks record button
        //TODO create a slide bar to have the user configure how loud the dB should be to make a sound
        //TODO Have a list of sounds the user can choose when the dB reaches the user's dB level
        //TODO Enable the user to use their own custom media
        //TODO provide explanation for the audio request

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



        //Used to test request audio recording permission as a toast message
        if(!isRecordAudioPermissionGranted())
        {
            Toast.makeText(getApplicationContext(), "Need to request permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No need to request permission", Toast.LENGTH_SHORT).show();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

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

    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

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

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    //For more detail change Integer to Double
    public void updateTv(){
        currentdb.setText(Integer.toString((getAmplitudeEMA())) + " dB");
    }


/*    public int soundDb(int ampl) {
        return (int) (20 * Math.log10(getAmplitudeEMA() / ampl));
    }*/

    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude()/50);
        else
            return 0;
    }

    public int getAmplitudeEMA() {
        int amp = (int) getAmplitude();
        mEMA = EMA_FILTER * amp + (1 - EMA_FILTER) * mEMA;
        return (int)  mEMA;
    }


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

/*    public void recordSound(View v) {
        soundDetector.startRecorder();
    }

    public void stopRecordingSound(View v) {
        soundDetector.stopRecorder();
    }

    private static final String TAG = "MyActivity";

    public void getamp(View v) {
        Log.i(TAG, "getAmplitude: ");
        soundDetector.getAmplitude();*/

////////////////////////////////////////////////////////////////////////////////////////////////////

// Helpful Resources

    //Measure dB
    //https://stackoverflow.com/questions/15693990/measuring-decibels-with-mobile-phone







}
