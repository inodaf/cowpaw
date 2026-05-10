package com.inodaf.cowpaw.config.di

import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventChannels {

    @Provides
    @Singleton
    fun transactionAddedChannel() = TransactionAddedChannel()
}