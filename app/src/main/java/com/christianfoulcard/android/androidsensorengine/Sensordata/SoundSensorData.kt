package com.christianfoulcard.android.androidsensorengine.Sensordata

import android.media.MediaRecorder
import android.os.Handler
import java.io.IOException

var mRecorder: MediaRecorder? = null

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