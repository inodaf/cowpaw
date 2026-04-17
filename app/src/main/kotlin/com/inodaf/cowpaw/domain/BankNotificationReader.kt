package com.inodaf.cowpaw.domain

interface BankNotificationReader {
    enum class TransactionType { Purchase, Reversal }

    fun amount(): Result<Long>
    fun type(): Result<TransactionType>
}