package com.inodaf.cowpaw.di

import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
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
}