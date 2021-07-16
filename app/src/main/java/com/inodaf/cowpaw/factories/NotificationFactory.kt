package com.inodaf.cowpaw.factories

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.MainActivity
import com.inodaf.cowpaw.R

@RequiresApi(Build.VERSION_CODES.O)
class NotificationFactory(
    private val context: Context,
    intent: Class<*>,
    private val cancelable: Boolean = false
) {
    private var notificationManager: NotificationManager
    private var notificationChannel: NotificationChannel
    private var notificationBuilder: Notification.Builder
    private var pendingIntent: PendingIntent

    private val channelID = "com.inodaf.cowpaw"
    private val channelName = "Cow Paw"

    init {
        val notificationIntent = Intent(context, intent)
        pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder = this.createBuilder()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun notify(title: String, id: Int = 1) {
        if (cancelable) cancel(id)

        if (notificationManager.activeNotifications.isEmpty()) {
            notificationBuilder.setContentTitle(title)
            notificationManager.notify(id, notificationBuilder.build())
        }
    }

    fun cancel(id: Int = 1) {
        notificationManager.cancel(id)
    }

    private fun createBuilder(): Notification.Builder {
        return Notification.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
    }
}