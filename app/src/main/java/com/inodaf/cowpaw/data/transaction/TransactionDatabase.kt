package com.inodaf.cowpaw.data.transaction

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase: RoomDatabase() {
  abstract fun getAmount(): TransactionDAO
}