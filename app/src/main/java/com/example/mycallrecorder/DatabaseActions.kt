package com.example.mycallrecorder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase



class DatabaseActions(activity: Context?) {
    var sqLiteDatabase=  DatabaseHandler(activity!!).writableDatabase
    fun addCallDetails(callDetails: CallDetails) {
        val values = ContentValues()
        values.put(DatabaseHandler.SERIAL_NUMBER, callDetails.serial)
        values.put(DatabaseHandler.PHONE_NUMBER, callDetails.num)
         values.put(DatabaseHandler.CONTACT_NAME,callDetails.name)
        values.put(DatabaseHandler.TIME, callDetails.time)
        values.put(DatabaseHandler.DATE, callDetails.date)
        sqLiteDatabase!!.insert(DatabaseHandler.TABLE_RECORD, null, values)
    }
    fun updateContactName(newContactName:String,contactName: String?){
        val values=ContentValues()
        values.put(DatabaseHandler.CONTACT_NAME,newContactName)
        sqLiteDatabase.update(DatabaseHandler.TABLE_RECORD,values,"contactName=?", arrayOf(contactName))
    }


    fun getallDetails(): ArrayList<CallDetails> {

        val recordList = ArrayList<CallDetails>()
        val selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_RECORD
        val cursor: Cursor = sqLiteDatabase.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val callDetails = CallDetails(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)

                )
                recordList.add(callDetails)
            } while (cursor.moveToNext())
        }
         return recordList
    }



}