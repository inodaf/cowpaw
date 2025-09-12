package com.inodaf.cowpaw.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.inodaf.cowpaw.usecases.ExpenseWatcher
import com.inodaf.cowpaw.utils.SMSDescriptor

class SMSHandler : Service() {
    private val smsDescriptor = SMSDescriptor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("CowPaw.SMSHandler", "Start SMS Handler Service")

        if (intent == null) return super.onStartCommand(intent, flags, startId)

        val message = intent.getStringExtra("message").toString()
        smsDescriptor.message = message

        if (smsDescriptor.isBankingSMS()) {
            Log.d("CowPaw.SMSHandler", "Match banking SMS")

            startService(Intent(this, ExpenseWatcher::class.java).apply {
                putExtra("bankingMessage", message)
                putExtra("isPurchase", smsDescriptor.isPurchaseSMS())
                putExtra("isReversal", smsDescriptor.isReversalSMS())
                putExtra("amount", smsDescriptor.getAmount())
            })
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
