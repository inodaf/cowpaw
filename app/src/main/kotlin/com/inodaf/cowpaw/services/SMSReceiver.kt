package com.inodaf.cowpaw.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val smsReceived = intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION

        if (!smsReceived) return

        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            val smsWatcher = Intent(context, SMSWatcher::class.java).apply {
                putExtra("RECEIVED_SMS", smsMessage.messageBody)
            }

            context.startService(smsWatcher)
        }
    }

}