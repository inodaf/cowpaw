package com.inodaf.cowpaw.usecases

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.inodaf.cowpaw.InvoiceWidget
import com.inodaf.cowpaw.MainActivity
import com.inodaf.cowpaw.R
import com.inodaf.cowpaw.factories.NotificationFactory

open class Base : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isPurchase = intent.getBooleanExtra("isPurchase", false)
        val isReversal = intent.getBooleanExtra("isReversal", false)

        val transactionAmount = intent.getStringExtra("amount").toString()
        val updatedInvoiceAmount = calculateNewAmount(isPurchase, isReversal, transactionAmount)

        Log.d("CowPaw.ExpenseWatcher", "Tx Amount $transactionAmount")
        Log.d("CowPaw.ExpenseWatcher", "New Amount $updatedInvoiceAmount")
        Log.d("CowPaw.ExpenseWatcher", "Purchase $isPurchase")
        Log.d("CowPaw.ExpenseWatcher", "Reversal $isReversal")

        postNotification(updatedInvoiceAmount)
        setNewAmount(updatedInvoiceAmount)

        updateWidgets()
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postNotification(amount: Float) {
        val formattedAmount = String.format("%.2f", amount)
        val title = "${getString(R.string.current_invoice)}: R$ $formattedAmount"

        NotificationFactory(this, MainActivity::class.java, true)
            .notify(title)
    }

    private fun setNewAmount(amount: Float) {
        val preferences = getSharedPreferences(
            getString(R.string.key_amount_file),
            MODE_PRIVATE
        )
        preferences.edit(commit = true) {
            putFloat(getString(R.string.key_amount_value), amount)
        }
    }

    fun getCurrentAmount(): Float {
        val preferences = getSharedPreferences(
            getString(R.string.key_amount_file),
            MODE_PRIVATE
        )

        return preferences.getFloat(
            getString(R.string.key_amount_value),
            0.0f
        )
    }

    private fun updateWidgets() {
        val ids = AppWidgetManager
            .getInstance(application)
            .getAppWidgetIds(ComponentName(applicationContext, InvoiceWidget::class.java))

        val intent = Intent(this, InvoiceWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }

        sendBroadcast(intent)
    }

    open fun calculateNewAmount(
        isPurchase: Boolean,
        isReversal: Boolean,
        amount: String
    ): Float = 0.0f

    override fun onBind(intent: Intent?): IBinder? = null
}

class ExpenseWatcher : Base() {
    override fun calculateNewAmount(
        isPurchase: Boolean,
        isReversal: Boolean,
        amount: String
    ): Float {
        val currentAmount = getCurrentAmount()
        val transactionAmount = amount.replace(",", ".").toFloat()

        if (isReversal) {
            return currentAmount - transactionAmount
        } else if (isPurchase) {
            return currentAmount + transactionAmount
        }

        return currentAmount
    }
}
