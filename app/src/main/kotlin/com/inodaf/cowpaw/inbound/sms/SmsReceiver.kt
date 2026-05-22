package com.inodaf.cowpaw.inbound.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.inodaf.cowpaw.inbound.sms.TransactionSmsParser.ParseError
import com.inodaf.cowpaw.usecases.RecordTransaction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var recordTransaction: RecordTransaction
    val parsers = listOf(ItauTransactionSmsParser())

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            parsers.forEach {
                it.parse(smsMessage.messageBody)
                    .onFailure { err -> mapFailure(err) }
                    .map { t -> recordTransaction(t) }
            }
        }
    }

    private fun mapFailure(error: Throwable) {
        when (error) {
            is ParseError -> Log.w("SmsReceiver", "Unable to parse SMS content. Not a transaction.")
            else -> Log.e("SmsReceiver", "Error parsing SMS: ${error.message}")
        }
    }
}