package com.christianfoulcard.android.androidsensorengine

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import androidx.work.*
import com.christianfoulcard.android.androidsensorengine.Sensors.SensorBatteryActivity
import java.lang.Exception
import java.util.concurrent.TimeUnit

var sensorBatteryActivity: SensorBatteryActivity? = null
val register = sensorBatteryActivity?.registerMyReceiver()
val notify = sensorBatteryActivity?.createNotificationChannel()

class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {




            //Creates the work request
            val uploadWorkRequest =
                    batteryInfo()

            Log.e("WorkManagerTesting", "Is it working")
                    PeriodicWorkRequestBuilder<BackgroundWorker>(10, TimeUnit.SECONDS)
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

private fun WorkManager.enqueueUniquePeriodicWork(uploadWorkRequest: Unit) {

}

fun batteryInfo() {
    register
    notify
}


