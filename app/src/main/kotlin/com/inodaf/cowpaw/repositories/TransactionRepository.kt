package com.inodaf.cowpaw.repositories

import com.inodaf.cowpaw.domain.Transaction

interface TransactionRepository {
    fun save(transaction: Transaction): Result<Unit>
    fun getAllSince(timestamp: Long, limit: Int = 20): Result<List<Transaction>>
}
