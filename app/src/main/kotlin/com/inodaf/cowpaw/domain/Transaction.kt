package com.inodaf.cowpaw.domain

import java.util.Date
import java.util.UUID

class Transaction private constructor(
    val id: UUID = UUID.randomUUID(),
    val type: Type,
    val amount: Cents,
    val createdAt: Date = Date(),
    val invoiceId: UUID,
) {
    enum class Type { Purchase, Refund, }

    companion object {
        fun purchase(amount: Long, invoiceId: UUID): Result<Transaction> {
            if (amount <= 0) return Result.failure(CreateError.InvalidAmount())
            val purchase = Transaction(
                type = Type.Purchase,
                amount = Cents(amount),
                invoiceId = invoiceId,
            )
            return Result.success(purchase)
        }

        fun refund(amount: Long, invoiceId: UUID): Result<Transaction> {
            if (amount <= 0) return Result.failure(CreateError.InvalidAmount())
            val refund = Transaction(
                type = Type.Refund,
                amount = Cents(amount),
                invoiceId = invoiceId,
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
