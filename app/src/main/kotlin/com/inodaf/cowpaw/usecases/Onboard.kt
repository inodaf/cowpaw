package com.inodaf.cowpaw.usecases

import com.inodaf.cowpaw.domain.Invoice
import com.inodaf.cowpaw.domain.InvoiceRepository
import java.util.Date
import javax.inject.Inject

class Onboard @Inject constructor(
    val invoiceRepository: InvoiceRepository,
) {
    operator fun invoke(input: Input): Result<Unit> {
        return Invoice.empty(dueAt = Date(input.invoiceDueDate))
            .let { invoiceRepository.save(it) }
            .onFailure { return Result.failure(it) }
    }

    data class Input(
        val invoiceDueDate: Long,
        val persistentNotification: Boolean,
    )
}