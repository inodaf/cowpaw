package com.inodaf.cowpaw.domain

import java.util.UUID

interface TransactionRepository {
    fun save(it: Transaction): Result<Unit>
    fun ofInvoice(invoiceId: UUID): Result<List<Transaction>>
}