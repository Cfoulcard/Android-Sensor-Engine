package com.sensors.audio

import android.app.Activity
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import com.androidsensorengine.utils.LogUtils.TAG
import java.io.File
import java.io.IOException

class AudioRecorder {

    //TODO work on file paths for different versions
    //TODO work on pause/resume flow

    private var isRecordingActive: Boolean = false

    /** Returns an output file string we can use to tie into the media recorder
     *
     * @param activity The activity we assign for access to the external files directory
     * */
    private fun createOutputFile(activity: Activity): String {
        val name = "audio.mp3"
        return "${filePath(activity)?.absolutePath}/$name"
    }

    /** Generates the file we need to tie into the output file */
    private fun filePath(activity: Activity): File? {
        return if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.S) {
            activity.getExternalFilesDir(Environment.DIRECTORY_RECORDINGS)
        } else {
            activity.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        }
    }

    /** Creates a [MediaRecorder] instance using the assigned [Activity]. Before we can use the
     * media recorder and measure audio we must first call this.
     * */
    fun createRecorder(activity: Activity) {
        try {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(createOutputFile(activity))
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "createRecorder: error configuring audio")
        } catch (e: RuntimeException) {
            Log.e(TAG, "createRecorder: File output error")
        }
    }

    /** Measures the audio. Can only be called after te recorder has been created */
    fun startRecorder() {
        try {
            recorder?.apply {
                prepare()
                start()
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "startRecorder: ${e.message}")
        } catch (e: IOException) {
            Log.e(TAG, "startRecorder: ${e.message}")
        }
    }

    fun pauseRecorder() {
        if (isRecordingActive) {
            try {
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pause()
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e(TAG, "pauseRecorder: ${e.message}")
            } catch (e: IOException) {
                Log.e(TAG, "pauseRecorder: ${e.message}")
            }
        }
    }

    fun resumeRecorder() {
        if (!isRecordingActive) {
            try {
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        resume()
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e(TAG, "pauseRecorder: ${e.message}")
            } catch (e: IOException) {
                Log.e(TAG, "pauseRecorder: ${e.message}")
            }
        }
    }

    /** Stops and releases the media recorder from measuring audio */
    fun destroyRecorder()  {
        recorder?.apply {
            try {
                stop()
                release()
                Log.d(TAG, "destroyRecorder: Destroyed")
            } catch (e: IllegalStateException) {
                Log.e(TAG, "destroyRecorder: ${e.message}")
            }
        }
    }

    companion object {
        var recorder: MediaRecorder? = null
    }
}