package com.christianfoulcard.android.androidsensorengine.Sensordata

import android.content.Context
import android.media.MediaRecorder
import android.widget.Toast
import java.io.IOException


var mRecorder: MediaRecorder? = null

//For sound recording + converting to sound data
//Handler is also used for pin shortcut dialog box

class SoundSensorData {



    //Properties of the microphone to start
     fun startRecorder() {
        if (mRecorder == null) {
            mRecorder = MediaRecorder()
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder!!.setOutputFile("/dev/null")
            try {
                mRecorder!!.prepare()
            } catch (ioe: IOException) {

            } catch (e: SecurityException) {

            }
            try {
                mRecorder!!.start()
            } catch (e: SecurityException) {

            }
        }
    }

    //Releases microphone when no longer in use
     fun stopRecorder() {
        if (mRecorder != null) {
            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null
        }
    }


}