package com.inodaf.cowpaw.persistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.domain.Invoice
import com.inodaf.cowpaw.domain.InvoiceRepository
import com.inodaf.cowpaw.domain.TransactionRepository
import java.util.UUID
import javax.inject.Inject

class InvoiceRepositorySqlite @Inject constructor(
    val sqlite: SQLiteOpenHelper,
    val transactionRepo: TransactionRepository,
) : InvoiceRepository {

    override fun save(it: Invoice): Result<Unit> {
        val db = sqlite.writableDatabase
        val values = ContentValues().apply {
            put("id", it.id.toString())
            put("amount", it.total().value)
            put("status", it.status.name)
            put("due_at", it.dueAt.time)
            put("paid_at", it.paidAt?.time)
            put("created_at", it.createdAt.time)
        }

        return runCatching {
            db.insertWithOnConflict("invoices", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        }
            .map { }
            .onFailure { err -> Result.failure<Unit>(err) }
            .also { db.close() }
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
}
