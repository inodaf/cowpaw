package com.inodaf.cowpaw.data.invoice

import androidx.room.Query
import androidx.room.Update

interface InvoiceDAO {
  @Query("SELECT amount FROM `transaction`")
  fun getTotalAmount(): Array<Float>

  @Query("SELECT * FROM `invoice` WHERE id = 1")
  fun get(id: String): Invoice

  @Update
  fun update(invoice: Invoice)
}