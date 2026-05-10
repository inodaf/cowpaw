package com.inodaf.cowpaw.domain

import java.util.Date
import java.util.UUID

class Transaction private constructor(
    invoiceId: UUID? = null,
    val id: UUID = UUID.randomUUID(),
    val type: Type,
    val amount: Cents,
    val createdAt: Date = Date(),
) {
    enum class Type { Purchase, Refund, }

    var invoiceId: UUID? = invoiceId; private set

    fun associateWithInvoice(invoiceId: UUID) {
        this.invoiceId = invoiceId
    }

    companion object {
        fun purchase(amount: Long): Result<Transaction> {
            if (amount <= 0) return Result.failure(CreateError.InvalidAmount())
            val purchase = Transaction(
                type = Type.Purchase,
                amount = Cents(amount),
            )
            return Result.success(purchase)
        }

        fun refund(amount: Long): Result<Transaction> {
            if (amount <= 0) return Result.failure(CreateError.InvalidAmount())
            val refund = Transaction(
                type = Type.Refund,
                amount = Cents(amount),
            )
            return Result.success(refund)
        }

        fun from(
            id: String,
            type: String,
            amount: Long,
            createdAt: Long,
            invoiceId: String,
        ) = Transaction(
            id = UUID.fromString(id),
            type = Type.valueOf(type),
            amount = Cents(amount),
            createdAt = Date(createdAt),
            invoiceId = UUID.fromString(invoiceId),
        )
    }

    sealed class CreateError : Throwable() {
        class InvalidAmount : CreateError()
    }
}
