package com.example.pocketmanager.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHandler(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    private val CREATE_BANK_TABLE =
        ("CREATE TABLE " + BANK_TABLE + "("
                + KEY_TYPE + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_BALANCE + " TEXT" + ")")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_BANK_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $BANK_TABLE")
        onCreate(db)
    }

    fun addBankSms(sms: SMS) {
        val db = this.writableDatabase
        val values: ContentValues
        values = ContentValues()
        values.put(KEY_TYPE, sms.msgType)
        values.put(KEY_AMOUNT, sms.msgAmt)
        values.put(KEY_DATE, sms.msgDate)
        values.put(KEY_BALANCE, sms.msgBal)
        db.insert(BANK_TABLE, null, values)
        db.close()
    }

    fun getAllSms(TABLE: String): List<SMS> {
        val smsList: MutableList<SMS> =
            ArrayList()
        val selectQuery = "SELECT * FROM $TABLE"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToLast()) {
            do {
                val sms = SMS()
                sms.msgType = cursor.getString(0)
                sms.msgAmt = cursor.getString(1)
                sms.msgDate = cursor.getString(2)
                sms.msgBal = cursor.getString(3)
                smsList.add(sms)
            } while (cursor.moveToPrevious())
        }
        return smsList
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SmsList"
        private const val BANK_TABLE = "bankTransactions"
        private const val KEY_TYPE = "type"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_DATE = "date"
        private const val KEY_BALANCE = "balance"
    }
}