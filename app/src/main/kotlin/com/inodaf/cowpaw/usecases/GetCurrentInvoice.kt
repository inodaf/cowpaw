package com.inodaf.cowpaw.usecases

import com.inodaf.cowpaw.domain.Cents
import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.domain.Transaction
import java.util.Date
import javax.inject.Inject

class GetCurrentInvoice(
    @Inject private val repository: InvoiceRepository
) {

    operator fun invoke(): Result<Output> {
        val result = repository.getForCurrentMonth()
        return result
            .map { Output(total = it.total(), dueAt = it.dueAt, transactions = it.transactions) }
            .onFailure { /* handle error if needed */ }
    }

    data class Output(
        val total: Cents,
        val dueAt: Date,
        val transactions: List<Transaction>
    )
}