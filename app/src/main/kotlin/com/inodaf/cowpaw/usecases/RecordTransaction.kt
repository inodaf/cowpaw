package com.inodaf.cowpaw.usecases

import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.domain.Notificator
import com.inodaf.cowpaw.domain.Transaction
import com.inodaf.cowpaw.domain.TransactionRepository
import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RecordTransaction @Inject constructor(
    val repository: TransactionRepository,
    val invoiceRepository: InvoiceRepository,
    val transactionAddedChannel: TransactionAddedChannel,
    val notificator: Notificator,
) {

    operator fun invoke(transaction: Transaction): Result<Unit> {
        val invoice = invoiceRepository.getForCurrentMonth()
            .getOrElse { return Result.failure(it) }

        transaction.associateWithInvoice(invoice.id)

        return repository.save(transaction).onSuccess {
            notificator.notify("")
            runBlocking { transactionAddedChannel.emit() }
        }
    }
}