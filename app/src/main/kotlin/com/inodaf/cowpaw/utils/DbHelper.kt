package com.inodaf.cowpaw.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "cowpaw.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE transactions (
                id TEXT PRIMARY KEY,
                type TEXT CHECK(type IN ('Purchase', 'Reversal')),
                amount INTEGER NOT NULL,
                created_at TEXT DEFAULT (datetime('now'))
            );
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS balance")
        onCreate(db)
    }
}
