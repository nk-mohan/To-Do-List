package com.makeover.todolist.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.makeover.todolist.R
import com.makeover.todolist.helper.vectorToBitmap
import com.makeover.todolist.utils.AppConstants
import com.makeover.todolist.view.dashboard.DashboardActivity

class ScheduleTaskWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val id = inputData.getInt(AppConstants.NOTIFICATION_ID, 0)
        val title = inputData.getString(AppConstants.NOTIFICATION_TITLE)
        val subTitle = inputData.getString(AppConstants.NOTIFICATION_SUB_TITLE)
        sendNotification(id, title, subTitle)

        return success()
    }

    private fun sendNotification(id: Int, title: String?, subTitle: String?) {

        val bundle = Bundle().apply {
            putInt(AppConstants.TASK_ID, id)
        }
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(DashboardActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.taskDetailsFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_event_schedule)
        val titleNotification = title ?: AppConstants.EMPTY_STRING
        val subtitleNotification = subTitle ?: AppConstants.EMPTY_STRING
        val notification =
            NotificationCompat.Builder(applicationContext, AppConstants.NOTIFICATION_CHANNEL)
                .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titleNotification).setContentText(subtitleNotification)
                .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(AppConstants.NOTIFICATION_CHANNEL)

            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    AppConstants.NOTIFICATION_CHANNEL,
                    AppConstants.NOTIFICATION_NAME,
                    IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

}