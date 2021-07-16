package com.inodaf.cowpaw.data.transaction

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TransactionDAO {
  @Query("SELECT amount FROM `transaction`")
  fun getAmount(): Array<Transaction>
}