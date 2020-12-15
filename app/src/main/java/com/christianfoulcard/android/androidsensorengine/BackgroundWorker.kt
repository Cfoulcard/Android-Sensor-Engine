/*
package com.christianfoulcard.android.androidsensorengine

import CHANNEL_ID
import VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import VERBOSE_NOTIFICATION_CHANNEL_NAME
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.christianfoulcard.android.androidsensorengine.sensors.SensorBatteryActivity
import java.lang.Exception

var sensorBatteryActivity: SensorBatteryActivity? = null
val register = sensorBatteryActivity?.registerMyReceiver()
val notify = sensorBatteryActivity?.createNotificationChannel()

class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {
            // Creates a notification
            makeStatusNotification("Hiiiii", applicationContext)

            //Creates the work request
            val uploadWorkRequest = OneTimeWorkRequestBuilder<BackgroundWorker>()
                    .build()



            val myWorkRequest = WorkManager.getInstance(applicationContext)
            myWorkRequest.enqueue(uploadWorkRequest)

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

fun makeStatusNotification(message: String, context: Context) {
    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("NOTIFICATION_TITLE")
            .setContentText("message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(1, builder.build())
}

fun batteryInfo() {
    register
    notify
}


*/
