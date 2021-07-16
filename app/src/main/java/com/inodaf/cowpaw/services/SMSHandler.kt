package com.inodaf.cowpaw.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.inodaf.cowpaw.usecases.ExpenseWatcher
import com.inodaf.cowpaw.utils.isFinancialMessage

class SMSHandler : Service() {

    private lateinit var message: String

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        message = intent.getStringExtra("message")

        if (isFinancialMessage(message)) {
            val purchaseIntent = Intent(this, ExpenseWatcher::class.java).apply {
                putExtra("message", message)
            }
            startService(purchaseIntent)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
