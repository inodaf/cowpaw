package com.inodaf.cowpaw.services

import android.app.IntentService
import android.content.Intent
import android.util.Log


class SMSWatcher : IntentService(SMSWatcher::class.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        val receivedSMSMessage = intent?.getStringExtra("RECEIVED_SMS")
        val operationHandler = Intent(this, SMSHandler::class.java).apply {
            putExtra("message", receivedSMSMessage)
        }

        startService(operationHandler)
        Log.d("CowPaw.SMSWatcher", receivedSMSMessage)
    }
}
