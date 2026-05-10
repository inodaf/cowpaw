package com.inodaf.cowpaw.config

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteDb(context: Context) : SQLiteOpenHelper(context, "cowpaw.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createInvoiceTable = """
            CREATE TABLE IF NOT EXISTS invoices (
                id TEXT PRIMARY KEY,
                amount INTEGER NOT NULL,
                status TEXT CHECK(status IN ('Open', 'Paid')) NOT NULL,
                due_at TEXT NOT NULL,
                paid_at TEXT,
                created_at TEXT DEFAULT (datetime('now'))
            )
        """.trimIndent()

        val createTransactionTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                id TEXT PRIMARY KEY,
                type TEXT CHECK(type IN ('Purchase', 'Refund')) NOT NULL,
                amount INTEGER NOT NULL,
                created_at TEXT DEFAULT (datetime('now')),
                invoice_id TEXT NOT NULL,
                FOREIGN KEY (invoice_id) REFERENCES invoices(id)
            );
        """.trimIndent()

        db.execSQL(createInvoiceTable)
        db.execSQL(createTransactionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS invoices")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }
}