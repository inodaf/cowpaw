package com.inodaf.cowpaw.inbound.sms

import com.inodaf.cowpaw.domain.Transaction

interface TransactionSmsParser {
    fun parse(content: String): Result<Transaction>

    sealed class ParseError : Throwable() {
        class NotATransaction : ParseError()
    }
}