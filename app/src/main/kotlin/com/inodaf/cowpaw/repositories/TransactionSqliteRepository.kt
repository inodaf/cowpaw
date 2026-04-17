package com.inodaf.cowpaw.repositories

import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.domain.Transaction
import javax.inject.Inject

class TransactionSqliteRepository(
    @Inject
    private val sqlite: SQLiteOpenHelper
) : TransactionRepository {

    override fun save(transaction: Transaction): Result<Unit> {
        val db = sqlite.writableDatabase
        val insertQuery = """
            INSERT INTO transactions (id, type, amount, created_at)
            VALUES (?, ?, ?, datetime('now'))
        """.trimIndent()


        return runCatching {
            val stmt = db.compileStatement(insertQuery).apply {
                bindString(1, transaction.id.toString())
                bindString(2, transaction.type.toString())
                bindLong(3, transaction.amount.toLong())
            }

            stmt.executeInsert()
            stmt.close()
        }
            .onSuccess { return Result.success(Unit) }
            .onFailure { return Result.failure(it) }
            .also { db.close() }
    }

    override fun getAllSince(timestamp: Long, limit: Int): Result<List<Transaction>> {
        TODO("Not yet implemented")
    }
}
