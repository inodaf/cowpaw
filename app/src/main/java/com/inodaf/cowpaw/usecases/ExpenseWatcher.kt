package com.inodaf.cowpaw.usecases

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.MainActivity
import com.inodaf.cowpaw.R
import com.inodaf.cowpaw.factories.NotificationFactory

open class Base : Service() {
    lateinit var notification : NotificationFactory

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val message: String = intent.getStringExtra("bankingMessage")
        val isPurchase: Boolean = intent.getBooleanExtra("isPurchase", false)
        val transactionAmount: String = intent.getStringExtra("amount")
        val updatedInvoiceAmount = parseMessage(message, isPurchase, transactionAmount)

        Log.d("CowPaw.ExpenseWatcher", "Amount ${updatedInvoiceAmount.toString()}")
        Log.d("CowPaw.ExpenseWatcher", "Purchase ${isPurchase.toString()}")

        setupNotification()
        updateNotification(updatedInvoiceAmount)

        setPersistedAmount(updatedInvoiceAmount.toString())
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotification() {
        notification = NotificationFactory(this, MainActivity::class.java, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNotification(amount: Float?) {
        notification.notify("${getString(R.string.current_invoice)}: R$ $amount")
    }

    private fun setPersistedAmount(amount: String?) {
        val persistenceLayer = getSharedPreferences(getString(R.string.key_amount_file), Context.MODE_PRIVATE)
        with (persistenceLayer.edit()) {
            putString(getString(R.string.key_amount_value), amount)
            commit()
        }
    }

    fun getPersistedAmount(): String? {
        val persistenceLayer =  getSharedPreferences(getString(R.string.key_amount_file), Context.MODE_PRIVATE)
        return persistenceLayer.getString(getString(R.string.key_amount_value), "0.0")
    }

    open fun parseMessage(message: String, isPurchase: Boolean, amount: String): Float? {
        return null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class ExpenseWatcher : Base() {
    override fun parseMessage(message: String, isPurchase: Boolean, amount: String): Float? {
        val previousAmount = getPersistedAmount()?.toFloatOrNull()

        return if (isPurchase) {
            val parsedPurchaseAmount = amount?.replace(",", ".")?.toFloatOrNull()
            return previousAmount?.let { parsedPurchaseAmount?.plus(it) }
        } else {
            previousAmount
        }
    }
}
