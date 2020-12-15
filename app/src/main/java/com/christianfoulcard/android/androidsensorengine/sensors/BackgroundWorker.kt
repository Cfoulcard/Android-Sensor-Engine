package com.christianfoulcard.android.androidsensorengine.sensors

import android.content.Context
import androidx.work.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

var sensorBatteryActivity:  SensorBatteryActivity? = null

class BackgroundWorker (appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {
            sensorBatteryActivity?.registerMyReceiver()
            sensorBatteryActivity?.createNotificationChannel()



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

