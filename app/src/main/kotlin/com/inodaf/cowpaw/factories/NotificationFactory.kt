package com.inodaf.cowpaw.factories

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.R

@RequiresApi(Build.VERSION_CODES.O)
class NotificationFactory(
    private val context: Context,
    private val intent: Class<*>,
    private val cancelable: Boolean = false
) {
    private val channelID = "com.inodaf.cowpaw"
    private val channelName = "Cow Paw"
    private var manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        manager.createNotificationChannel(
            NotificationChannel(
                channelID, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    fun notify(title: String, id: Int = 1) {
        if (cancelable) manager.cancel(id)
        if (!manager.activeNotifications.isEmpty()) return

        val intent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, intent),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, channelID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(intent)
            .setContentTitle(title)
            .setOngoing(true)
            .build()

        manager.notify(id, notification)
    }
}