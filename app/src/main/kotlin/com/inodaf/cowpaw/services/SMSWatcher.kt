package com.inodaf.cowpaw.services

import android.app.IntentService
import android.content.Intent
import android.util.Log


class SMSWatcher : IntentService(SMSWatcher::class.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return

        val receivedSMSMessage = intent.getStringExtra("RECEIVED_SMS").toString()
        val smsHandler = Intent(this, SMSHandler::class.java).apply {
            putExtra("message", receivedSMSMessage)
        }

        startService(smsHandler)
        Log.d("CowPaw.SMSWatcher", receivedSMSMessage)
    }
}
