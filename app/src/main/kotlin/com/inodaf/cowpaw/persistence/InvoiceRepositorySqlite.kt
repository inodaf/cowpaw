package com.inodaf.cowpaw.persistence

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.domain.Invoice
import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.domain.TransactionRepository
import java.util.UUID
import javax.inject.Inject

class InvoiceRepositorySqlite(
    @Inject private val sqlite: SQLiteOpenHelper,
    @Inject private val transactionRepo: TransactionRepository,
) : InvoiceRepository {

    override fun getOpen(): Float {
        val db = sqlite.readableDatabase

        val cursor = db.query("balance", arrayOf("amount"), null, null, null, null, null, null)
        val amount = if (cursor.moveToFirst()) {
            cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
        } else 0.0f

        cursor.close()
        db.close()

        return amount
    }

    override fun getForCurrentMonth(): Result<Invoice> {
        val db = sqlite.readableDatabase
        val query = """
            SELECT id, status, due_at, paid_at, created_at
            FROM invoices
            WHERE strftime('%Y-%m', created_at / 1000, 'unixepoch') = strftime('%Y-%m', 'now')
            LIMIT 1
        """.trimIndent()

        return runCatching {
            val cursor = db.rawQuery(query, null)
            if (!cursor.moveToFirst()) {
                cursor.close()
                throw NoSuchElementException()
            }

            val transactions = transactionRepo
                .ofInvoice(UUID.fromString(cursor.getString(0)))
                .getOrElse { emptyList() }

            val invoice = Invoice.from(
                id = cursor.getString(0),
                status = cursor.getString(1),
                dueAt = cursor.getLong(2),
                paidAt = cursor.getLong(3),
                createdAt = cursor.getLong(4),
                transactions = transactions,
            )

            cursor.close()
            invoice
        }
            .onFailure { err -> return Result.failure(err) }
            .also { db.close() }
    }

    override fun setCurrent(amount: Float) {
        val db = sqlite.writableDatabase
        val values = ContentValues().apply { put("amount", amount) }

        db.insert("balance", null, values)
        db.close()
    }
}