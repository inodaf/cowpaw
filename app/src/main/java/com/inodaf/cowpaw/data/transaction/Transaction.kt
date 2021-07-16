package com.inodaf.cowpaw.data.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity
data class Transaction(
  @ColumnInfo(name = "amount") val amount: Float,
  @ColumnInfo(name = "created_at") val createdAt: Date
)