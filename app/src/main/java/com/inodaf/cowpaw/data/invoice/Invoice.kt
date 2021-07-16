package com.inodaf.cowpaw.data.invoice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
  @PrimaryKey private val id: String,
  @ColumnInfo(name = "total_amount") val totalAmount: Float
)