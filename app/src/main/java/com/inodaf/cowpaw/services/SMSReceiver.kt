package com.inodaf.cowpaw.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi

class SMSReceiver : BroadcastReceiver() {
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onReceive(context: Context, intent: Intent?) {
    val isSMSReceived = intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION

    if (isSMSReceived) {
      for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
        val message = smsMessage.messageBody
        val watcherServiceIntent = Intent(context, SMSWatcher::class.java).apply {
          putExtra("RECEIVED_SMS", message)
        }

        context.startService(watcherServiceIntent)
      }
    }
  }
}