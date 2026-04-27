package com.inodaf.cowpaw.domain

import java.util.Date
import java.util.UUID

class Invoice private constructor(
    status: Status,
    paidAt: Date? = null,
    val id: UUID = UUID.randomUUID(),
    val dueAt: Date,
    val createdAt: Date = Date(),
    val transactions: List<Transaction>,
) {
    enum class Status { Open, Paid }

    var status: Status = status; private set
    var paidAt: Date? = paidAt; private set

    fun isOverdue(): Boolean {
        return status == Status.Open && Date() > dueAt
    }

    /**
     * Mark the Invoice as [Status.Paid]. If already paid,
     * return a [PayError].
     */
    fun pay(): Result<Unit> {
        if (status == Status.Paid) return Result.failure(PayError.AlreadyPaid())

        status = Status.Paid
        paidAt = Date()

        return Result.success(Unit)
    }

    /**
     * Based on all associated [Transaction] calculate
     * the total amount due for this Invoice.
     */
    fun total(): Cents {
        return transactions.fold(Cents(value = 0L)) { acc, transaction ->
            when (transaction.type) {
                Transaction.Type.Purchase -> Cents(value = acc.value + transaction.amount.value)
                Transaction.Type.Refund -> Cents(value = acc.value - transaction.amount.value)
            }
        }
    }

    companion object {
        fun empty(dueAt: Date) = Invoice(
            status = Status.Open,
            dueAt = dueAt,
            transactions = listOf()
        )

        fun from(
            id: String,
            status: String,
            dueAt: Long,
            paidAt: Long?,
            createdAt: Long,
            transactions: List<Transaction>,
        ) = Invoice(
            id = UUID.fromString(id),
            status = Status.valueOf(status),
            dueAt = Date(dueAt),
            paidAt = paidAt?.let { Date(it) },
            createdAt = Date(createdAt),
            transactions = transactions,
        )
    }

    sealed class PayError : Throwable() {
        class AlreadyPaid : PayError()
    }
}
