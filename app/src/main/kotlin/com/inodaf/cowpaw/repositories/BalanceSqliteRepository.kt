package com.inodaf.cowpaw.repositories

import android.content.ContentValues
import android.content.Context
import com.inodaf.cowpaw.utils.DbHelper

class BalanceSqliteRepository(private val context: Context) : BalanceRepository {
    private val helper: DbHelper = DbHelper(context)

    override fun getCurrent(): Float {
        val db = helper.readableDatabase

        val cursor = db.query("balance", arrayOf("amount"), null, null, null, null, null, null)
        val amount = if (cursor.moveToFirst()) {
            cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
        } else 0.0f

        cursor.close()
        db.close()

        return amount
    }

    override fun setCurrent(amount: Float) {
        val db = helper.writableDatabase
        val values = ContentValues().apply { put("amount", amount) }

        db.insert("balance", null, values)
        db.close()
    }
}