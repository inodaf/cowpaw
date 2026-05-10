package com.inodaf.cowpaw.outbound

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
import com.inodaf.cowpaw.domain.Notificator
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class InvoiceTotalNotificator @Inject constructor(
    private val context: Context,
    private val currentInvoice: GetCurrentInvoice
) : Notificator {
    private val channelId = "com.inodaf.cowpaw.invoice_amount_channel"
    private val channelName = "Cow Paw"

    private var manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        manager.createNotificationChannel(
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    override fun send() {
        manager.cancel(1)
        if (!manager.activeNotifications.isEmpty()) return

        val invoice = currentInvoice().getOrNull() ?: return
        val title = context.getString(R.string.current_invoice) + ": " + invoice.total.format()

        val intent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(intent)
            .setContentTitle(title)
            .setOngoing(true)
            .build()

        manager.notify(1, notification)
    }
}