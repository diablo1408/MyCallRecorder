package com.example.mycallrecorder

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import java.io.File
import java.util.*


class SystemMethods {

    var cal: Calendar = Calendar.getInstance()
    val systemDate: String
        get() {
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH) + 1
            val day: Int = cal.get(Calendar.DATE)
            val date = day.toString() + "_" + month.toString() + "_" + year.toString()
            return date
        }

    val systemTime: String
        get() {
            var am_pm = ""
            val sec: Int = cal.get(Calendar.SECOND)
            val min: Int = cal.get(Calendar.MINUTE)
            val hr: Int = cal.get(Calendar.HOUR)
            val amPm: Int = cal.get(Calendar.AM_PM)
            if (amPm == 1) am_pm = "PM" else if (amPm == 0) am_pm = "AM"
            val time =
                "$hr:$min:$sec $am_pm"

            return time
        }

    val systemPath: String
        get() {
            val internalFile = systemDate
            val file = File(Environment.getExternalStorageDirectory().toString() + "/My Records/")
            val file1 =
                File(Environment.getExternalStorageDirectory().toString() + "/My Records/" + internalFile + "/")
            if (!file.exists()) {
                file.mkdir()
            }
            if (!file1.exists()) file1.mkdir()
            val path: String = file1.getAbsolutePath()

            return path
        }

    fun getContactName(number: String?, context: Context): String {
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val projection =
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName = ""
        val cursor: Cursor? = context.getContentResolver().query(uri, projection, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0)
            }
            cursor.close()
        }
        return if (contactName != null && contactName != "") contactName else "UNKNOWN"
    }
}