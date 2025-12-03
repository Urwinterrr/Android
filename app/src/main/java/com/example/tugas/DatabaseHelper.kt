// Lokasi file: app/src/main/java/com/example/tugas/DatabaseHelper.kt

package com.example.tugas

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tugas.model.TransactionModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "FinancialRecord.db"

        // Konstanta untuk tabel transaksi
        const val TABLE_TRANSACTIONS = "transactions_table"
        const val COL_ID = "id"
        const val COL_AMOUNT = "amount"
        const val COL_TYPE = "type"
        const val COL_CATEGORY = "category"
        const val COL_NOTE = "note"
        const val COL_DATE = "date"

        // Konstanta untuk tabel PIN
        const val TABLE_PIN = "pin_table"
        const val COL_PIN_ID = "id"
        const val COL_PIN = "pin"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Buat tabel transaksi
        val createTransactionsTableQuery = "CREATE TABLE $TABLE_TRANSACTIONS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_AMOUNT INTEGER NOT NULL," +
                "$COL_TYPE TEXT NOT NULL," +
                "$COL_CATEGORY TEXT NOT NULL," +
                "$COL_NOTE TEXT," +
                "$COL_DATE TEXT NOT NULL)"
        db?.execSQL(createTransactionsTableQuery)

        // Buat tabel PIN
        val createPinTableQuery = "CREATE TABLE $TABLE_PIN (" +
                "$COL_PIN_ID INTEGER PRIMARY KEY," +
                "$COL_PIN TEXT NOT NULL)"
        db?.execSQL(createPinTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PIN")
        onCreate(db)
    }

    // =================================================================
    // BAGIAN YANG PERLU DIISI KEMBALI
    // =================================================================

    fun addTransaction(transaction: TransactionModel) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_AMOUNT, transaction.amount)
            put(COL_TYPE, transaction.type)
            put(COL_CATEGORY, transaction.category)
            put(COL_NOTE, transaction.note)
            put(COL_DATE, transaction.date)
        }
        db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllTransactions(): List<TransactionModel> {
        val transactionList = mutableListOf<TransactionModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TRANSACTIONS ORDER BY $COL_ID DESC"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val transaction = TransactionModel(
                    id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT)),
                    type = cursor.getString(cursor.getColumnIndex(COL_TYPE)),
                    category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                    note = cursor.getString(cursor.getColumnIndex(COL_NOTE)),
                    date = cursor.getString(cursor.getColumnIndex(COL_DATE))
                )
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return transactionList
    }

    @SuppressLint("Range")
    fun getTransactionById(transactionId: Int): TransactionModel? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TRANSACTIONS WHERE $COL_ID = $transactionId"
        val cursor = db.rawQuery(query, null)
        var transaction: TransactionModel? = null

        if (cursor.moveToFirst()) {
            transaction = TransactionModel(
                id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT)),
                type = cursor.getString(cursor.getColumnIndex(COL_TYPE)),
                category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                note = cursor.getString(cursor.getColumnIndex(COL_NOTE)),
                date = cursor.getString(cursor.getColumnIndex(COL_DATE))
            )
        }
        cursor.close()
        db.close()
        return transaction
    }

    fun updateTransaction(transaction: TransactionModel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_AMOUNT, transaction.amount)
            put(COL_TYPE, transaction.type)
            put(COL_CATEGORY, transaction.category)
            put(COL_NOTE, transaction.note)
            put(COL_DATE, transaction.date)
        }
        val result = db.update(TABLE_TRANSACTIONS, values, "$COL_ID = ?", arrayOf(transaction.id.toString()))
        db.close()
        return result
    }

    fun deleteTransaction(transactionId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TRANSACTIONS, "$COL_ID = ?", arrayOf(transactionId.toString()))
        db.close()
    }

    // --- FUNGSI UNTUK PIN ---

    fun savePin(pin: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PIN_ID, 1)
            put(COL_PIN, pin)
        }
        db.replace(TABLE_PIN, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getPin(): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COL_PIN FROM $TABLE_PIN WHERE $COL_PIN_ID = 1", null)
        var savedPin: String? = null
        if (cursor.moveToFirst()) {
            savedPin = cursor.getString(cursor.getColumnIndex(COL_PIN))
        }
        cursor.close()
        db.close()
        return savedPin
    }
}
