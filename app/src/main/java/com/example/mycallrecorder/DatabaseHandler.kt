package com.example.mycallrecorder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
      override  fun onCreate(db:SQLiteDatabase) {
          val CREATE_LOG_TABLE = ("CREATE TABLE " + TABLE_RECORD + "("
                + SERIAL_NUMBER + " INTEGER PRIMARY KEY," + PHONE_NUMBER + " TEXT,"+ CONTACT_NAME + " TEXT," + TIME + " TEXT,"
                + DATE + " TEXT" + ")")
          db.execSQL(CREATE_LOG_TABLE)
    }
  override  fun onUpgrade(db: SQLiteDatabase, oldVersion:Int, newVersion:Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORD")
        onCreate(db)
    }
    companion object {
         private const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "callRecords"
        const val TABLE_RECORD = "callRecord"
       const  val SERIAL_NUMBER = "serialNumber"
       const val PHONE_NUMBER = "phoneNumber"
       const val CONTACT_NAME="contactName"
       const val TIME = "time"
       const val DATE = "date"
    }
}