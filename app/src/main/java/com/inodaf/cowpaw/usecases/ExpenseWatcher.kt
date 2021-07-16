package com.inodaf.cowpaw.usecases

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.MainActivity
import com.inodaf.cowpaw.R
import com.inodaf.cowpaw.factories.NotificationFactory

open class Base : Service() {
    lateinit var notification : NotificationFactory

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val message = intent.getStringExtra("message")
        val amount = parseMessage(message)

        setupNotification()
        updateNotification(amount)

        setPersistedAmount(amount.toString())
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotification() {
        notification = NotificationFactory(this, MainActivity::class.java, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNotification(amount: Float?) {
        notification.notify("${getString(R.string.current_invoice)}: $$amount")
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

    open fun parseMessage(message: String): Float? {
        return null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class ExpenseWatcher : Base() {
    private fun isPurchaseTransaction(message: String): Boolean {
        val matcher = getString(R.string.matcher_approved_purchase)
        return message.contains(Regex(matcher, RegexOption.IGNORE_CASE))
    }

    override fun parseMessage(message: String): Float? {
        val previousAmount = getPersistedAmount()?.toFloatOrNull()
        val amountMatchingRule = Regex("\\d*.\\d*,\\d{2}")

        return if (isPurchaseTransaction(message)) {
            val matchingAmount = amountMatchingRule.find(message)
            val operationAmount = matchingAmount?.value?.replace(",", ".")?.toFloatOrNull()

            return previousAmount?.let { operationAmount?.plus(it) }
        } else {
            previousAmount
        }
    }
}
