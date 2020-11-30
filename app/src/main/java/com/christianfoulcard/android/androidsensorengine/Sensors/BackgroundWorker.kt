package com.christianfoulcard.android.androidsensorengine.Sensors

import android.content.Context
import android.hardware.SensorEvent
import androidx.work.*
import com.christianfoulcard.android.androidsensorengine.Sensors.SensorPressureActivity
import java.lang.Exception
import java.util.concurrent.TimeUnit

val SENSOR_PRESSURE_ACTIVITY: SensorPressureActivity? = null

class BackgroundWorker (appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {



            //Creates the work request
            val uploadWorkRequest =
                    PeriodicWorkRequestBuilder<BackgroundWorker>(15, TimeUnit.MINUTES)
                            .build()

            val myWorkRequest = WorkManager.getInstance(applicationContext)
            myWorkRequest.enqueueUniquePeriodicWork(uploadWorkRequest)


            //WorkManager service has succeeded
            return Result.success()

        } catch (e: Exception) {
            //WorkManager service has failed
            return Result.failure()
        }
    }
}

private fun WorkManager.enqueueUniquePeriodicWork(uploadWorkRequest: PeriodicWorkRequest) {

}

