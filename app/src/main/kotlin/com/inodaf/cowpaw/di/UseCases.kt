package com.inodaf.cowpaw.di

import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.domain.Notificator
import com.inodaf.cowpaw.domain.TransactionRepository
import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import com.inodaf.cowpaw.usecases.Onboard
import com.inodaf.cowpaw.usecases.RecordTransaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCases {

    @Provides
    @Singleton
    fun getCurrentInvoice(
        invoiceRepository: InvoiceRepository,
    ) = GetCurrentInvoice(invoiceRepository)

    @Provides
    @Singleton
    fun recordTransaction(
        transactionRepository: TransactionRepository,
        invoiceRepository: InvoiceRepository,
        transactionAddedChannel: TransactionAddedChannel,
        notificator: Notificator,
    ) = RecordTransaction(
        transactionRepository,
        invoiceRepository,
        transactionAddedChannel,
        notificator
    )

    @Provides
    @Singleton
    fun onboard(
        invoiceRepository: InvoiceRepository,
    ) = Onboard(invoiceRepository)
}