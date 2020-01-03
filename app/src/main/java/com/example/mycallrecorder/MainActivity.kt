package com.example.mycallrecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_layout.*


class MainActivity : AppCompatActivity() {

    var callDetailsList= ArrayList<CallDetails>()
    var recordAdapter=RecordAdapter(callDetailsList)
     var  contactName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val linearLayout = LinearLayoutManager(this)
        recordlist.layoutManager = linearLayout

        recordlist.adapter = recordAdapter
        val behavior = BottomSheetBehavior.from(bottomsheetView)
        val behavior2 = BottomSheetBehavior.from(bottomsheetView2)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior2.state = BottomSheetBehavior.STATE_HIDDEN


        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val mSettingsIntent = Intent(Intent.ACTION_MAIN)
                .setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                .setData(Uri.parse("package:$packageName"))

            try {
                startActivity(mSettingsIntent)
            } catch (ex: Exception) {

            }
        }

        recordAdapter.longClickListener = object : onLongClicklistener {
            override fun onLongClick(callitem: CallDetails): Boolean {
                Log.d("item", "item is clicked")
                if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    Log.d("item2", "state is expanded")
                }
                contactName = callitem.name
                return true
            }

        }
        recordAdapter.clickListener = object : onClickListener {
            override fun onClick() {
                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN

                }

            }

        }
        renamebtn.setOnClickListener {
            if (behavior2.state == BottomSheetBehavior.STATE_HIDDEN) {
                behavior2.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.state = BottomSheetBehavior.STATE_HIDDEN

            }
        }
        okbtn.setOnClickListener {
            if (edView.text.isEmpty()) {
                Toast.makeText(this, "enter text first to proceed", Toast.LENGTH_SHORT).show()
            } else {
                val newContactName = edView.text.toString()
                DatabaseActions(this).updateContactName(newContactName, contactName)
                behavior2.state = BottomSheetBehavior.STATE_HIDDEN
                displayRecords()
            }
        }
        cancelsheet.setOnClickListener {
            if (behavior2.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior2.state = BottomSheetBehavior.STATE_HIDDEN


            }

        }
    }
       override fun onResume() {
        super.onResume()
           val pref = PreferenceManager.getDefaultSharedPreferences(this)
           if (pref.getInt("callNumData", 0) == 0) {
               recordStatus.visibility = View.VISIBLE
           } else {
               recordStatus.visibility = View.INVISIBLE

           }

           displayRecords()
        }


       fun displayRecords()
       { val callrecords= DatabaseActions(this).getallDetails()
         callDetailsList.clear()
         callDetailsList.addAll(callrecords)
         recordAdapter.notifyDataSetChanged()
       }
      private fun checkPermission(): Boolean {
        var i = 0
        val perm = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
        )
        val reqPerm: ArrayList<String> = ArrayList()
        for (permis in perm) {
            val resultPhone = ContextCompat.checkSelfPermission(this@MainActivity, permis)
            if (resultPhone == PackageManager.PERMISSION_GRANTED) i++ else {
                reqPerm.add(permis)
            }
        }
         return if (i == 5) true else  requestPermission(reqPerm)
      }

       private fun requestPermission(perm: ArrayList<String>): Boolean {
          var listReq = arrayOfNulls<String>(perm.size)
           listReq = perm.toArray<String>(listReq)
           for (permissions in perm) {
             if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissions)) {
                Toast.makeText(applicationContext, "Phone Permissions needed for $permissions", Toast.LENGTH_LONG)
             }
           }
            ActivityCompat.requestPermissions(this@MainActivity, listReq, 1)
           return false
          }
          override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
             when (requestCode) {
               1 -> if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED)
                   Toast.makeText(applicationContext, "Permission Granted to access Phone calls", Toast.LENGTH_LONG)
                 else Toast.makeText(applicationContext, "You can't access Phone calls", Toast.LENGTH_LONG)
         }
    }




}



