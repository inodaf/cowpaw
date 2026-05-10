package com.inodaf.cowpaw.config.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.inodaf.cowpaw.domain.Notificator
import com.inodaf.cowpaw.outbound.InvoiceTotalNotificator
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Notificators {

    @Singleton
    @Provides
    @RequiresApi(Build.VERSION_CODES.O)
    fun invoiceTotalNotificator(
        @ApplicationContext context: Context,
        currentInvoice: GetCurrentInvoice,
    ): Notificator = InvoiceTotalNotificator(
        context,
        currentInvoice
    )
}