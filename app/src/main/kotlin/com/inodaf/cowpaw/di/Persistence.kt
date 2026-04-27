package com.inodaf.cowpaw.di

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.domain.TransactionRepository
import com.inodaf.cowpaw.persistence.InvoiceRepositorySqlite
import com.inodaf.cowpaw.persistence.TransactionRepositorySqlite
import com.inodaf.cowpaw.utils.SqliteDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Persistence {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context
    ): SQLiteOpenHelper = SqliteDb(context)

    @Provides
    @Singleton
    fun transactionRepository(
        sqlite: SQLiteOpenHelper
    ) = TransactionRepositorySqlite(sqlite)

    @Provides
    @Singleton
    fun invoiceRepository(
        sqlite: SQLiteOpenHelper,
        transactionRepo: TransactionRepository
    ) = InvoiceRepositorySqlite(sqlite, transactionRepo)
}

