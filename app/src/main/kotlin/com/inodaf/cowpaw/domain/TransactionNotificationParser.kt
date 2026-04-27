package com.inodaf.cowpaw.domain

interface TransactionNotificationParser {
    fun parse(content: String): Result<Transaction>
}