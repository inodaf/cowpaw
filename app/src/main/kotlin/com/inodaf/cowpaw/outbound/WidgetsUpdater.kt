package com.inodaf.cowpaw.outbound

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import com.inodaf.cowpaw.InvoiceWidget
import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WidgetsUpdater : Service() {

    @Inject lateinit var transactionAddedChannel: TransactionAddedChannel
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            transactionAddedChannel.flow.collect {
                updateAll()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun updateAll() {
        val ids = AppWidgetManager
            .getInstance(application)
            .getAppWidgetIds(ComponentName(applicationContext, InvoiceWidget::class.java))

        val intent = Intent(this, InvoiceWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }

        sendBroadcast(intent)
    }
}