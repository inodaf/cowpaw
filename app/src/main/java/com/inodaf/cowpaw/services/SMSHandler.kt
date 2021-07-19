package com.inodaf.cowpaw.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.inodaf.cowpaw.usecases.ExpenseWatcher
import com.inodaf.cowpaw.utils.SMSDescriptor

class SMSHandler : Service() {

    private lateinit var message: String
    private lateinit var smsDescriptor: SMSDescriptor

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        message = intent.getStringExtra("message")
        smsDescriptor = SMSDescriptor(message)

        if (smsDescriptor.matchBankingSMS()) {
            Log.d("CowPaw.SMSHandler", "matchBankingSMS")

            startService(Intent(this, ExpenseWatcher::class.java).apply {
                putExtra("bankingMessage", message)
                putExtra("isPurchase", smsDescriptor.matchPurchaseSMS())
                putExtra("isReversal", smsDescriptor.matchReversalSMS())
                putExtra("amount", smsDescriptor.getAmount())
            })
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
