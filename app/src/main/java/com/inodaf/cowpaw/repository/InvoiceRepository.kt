package com.inodaf.cowpaw.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.inodaf.cowpaw.R
import com.inodaf.cowpaw.models.Invoice

class InvoiceRepository() {

  public fun get(): Invoice? {
    val invoice = getFromPreferences(context.getString(R.string.key_amount_file))
    return invoice.getString(context.getString(R.string.key_amount_value), "0")?.let {
      Invoice(it.toFloat())
    }
  }

  private fun getFromPreferences(prefKey: String): SharedPreferences {
    return context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
  }
}