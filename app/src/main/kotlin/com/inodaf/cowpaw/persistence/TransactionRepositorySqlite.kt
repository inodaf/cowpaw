package com.inodaf.cowpaw.persistence

import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.domain.Transaction
import com.inodaf.cowpaw.domain.TransactionRepository
import java.util.UUID
import javax.inject.Inject

class TransactionRepositorySqlite(
    @Inject private val sqlite: SQLiteOpenHelper
) : TransactionRepository {

    override fun save(it: Transaction): Result<Unit> {
        val db = sqlite.writableDatabase
        val insertQuery = """
            INSERT INTO transactions (id, type, amount, created_at, invoice_id)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()


        return runCatching {
            val stmt = db.compileStatement(insertQuery).apply {
                bindString(1, it.id.toString())
                bindString(2, it.type.toString())
                bindLong(3, it.amount.value)
                bindLong(4, it.createdAt.time)
                bindString(5, it.invoiceId.toString())
            }

            stmt.executeInsert()
            stmt.close()
        }
            .onSuccess { return Result.success(Unit) }
            .onFailure { err -> return Result.failure(err) }
            .also { db.close() }
    }

    override fun ofInvoice(invoiceId: UUID): Result<List<Transaction>> {
        val db = sqlite.readableDatabase
        val query = """
            SELECT id, type, amount, created_at, invoice_id
            FROM transactions
            WHERE invoice_id = ?
        """.trimIndent()

        return runCatching {
            val cursor = db.rawQuery(query, arrayOf(invoiceId.toString()))
            val transactions = mutableListOf<Transaction>()

            while (cursor.moveToNext()) {
                val transaction = Transaction.from(
                    id = cursor.getString(0),
                    type = cursor.getString(1),
                    amount = cursor.getLong(2),
                    createdAt = cursor.getLong(3),
                    invoiceId = cursor.getString(4),
                )
                transactions.add(transaction)
            }
            cursor.close()
            transactions
        }
            .onSuccess { return Result.success(it) }
            .onFailure { err -> return Result.failure(err) }
            .also { db.close() }
    }
}
