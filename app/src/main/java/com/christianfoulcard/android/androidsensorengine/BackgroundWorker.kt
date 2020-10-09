package com.christianfoulcard.android.androidsensorengine

import android.content.Context
import androidx.work.*
import com.christianfoulcard.android.androidsensorengine.Sensors.TemperatureActivity
import java.lang.Exception
import java.util.concurrent.TimeUnit

class BackgroundWorker (appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {
    override fun doWork(): Result {
        try {

          //    runTemperatureInBackground()

            //Creates the work request
            val uploadWorkRequest =
                    PeriodicWorkRequestBuilder<BackgroundWorker>(1, TimeUnit.MINUTES)
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

