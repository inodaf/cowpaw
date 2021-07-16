package com.inodaf.cowpaw.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.services.SMSWatcher

class SMSReceiver : BroadcastReceiver() {
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onReceive(context: Context, intent: Intent?) {
    val isSmsReceivedAction = intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION

    if (isSmsReceivedAction) {
      for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
        val messageBody = smsMessage.messageBody
        val serviceIntent = Intent(context, SMSWatcher::class.java).apply {
          putExtra("RECEIVED_SMS", messageBody)
        }

        context.startService(serviceIntent)
      }
    }
  }
}