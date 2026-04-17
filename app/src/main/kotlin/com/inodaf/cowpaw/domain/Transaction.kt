package com.inodaf.cowpaw.domain

import java.time.OffsetDateTime
import java.util.UUID

class Transaction private constructor(
    val id: UUID = UUID.randomUUID(),
    val type: Type,
    val amount: Int,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    enum class Type {
        Purchase,
        Reversal,
    }

    companion object {
        fun createPurchase(amount: Int): Transaction {
            require(amount > 0) { "Purchase amount must be positive" }
            return Transaction(type = Type.Purchase, amount = amount)
        }

        fun createReversal(amount: Int): Transaction {
            require(amount > 0) { "Reversal amount must be positive" }
            return Transaction(type = Type.Reversal, amount = amount)
        }
    }
}
