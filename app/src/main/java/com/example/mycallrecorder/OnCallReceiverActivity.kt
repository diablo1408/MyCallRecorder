package com.example.mycallrecorder

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.MediaRecorder
import android.preference.PreferenceManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.popup_layout.view.*


 class OnCallReceiverActivity :BroadcastReceiver() {
     var numofcalls=0
     override fun onReceive(context: Context?, intent: Intent?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)
            || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)
        ) {
            val mFloatingView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null)
            val mWindowManager = context?.getSystemService(Service.WINDOW_SERVICE) as WindowManager
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            mWindowManager.addView(mFloatingView, params)
            mFloatingView.findViewById<View>(R.id.popupbox).setOnTouchListener(object :
                View.OnTouchListener {
                private var initialX: Int = 0
                private var initialY: Int = 0
                private var initialTouchX: Float = 0.toFloat()
                private var initialTouchY: Float = 0.toFloat()
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    Log.v("move", "action move")

                    when (event.getAction()) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.getRawX()
                            initialTouchY = event.getRawY()
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {

                            //this code is helping the widget to move around the screen with fingers
                            params.x = initialX + (event.getRawX() - initialTouchX).toInt()
                            params.y = initialY + (event.getRawY() - initialTouchY).toInt()
                            mWindowManager.updateViewLayout(mFloatingView, params)
                            return true
                        }
                    }
                    return false
                }
            })
            mFloatingView.recordbtn.setOnClickListener {
                it.setBackgroundColor(Color.RED)
                Toast.makeText(context, "recording started", Toast.LENGTH_SHORT).show()
                val recordStart = Intent(context, RecorderService::class.java)
                var phoneNumber = intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                recordStart.putExtra("number", phoneNumber)
                context.startService(recordStart)
                var serialNumber: Int = pref.getInt("serialNumData", 1)
                numofcalls = pref.getInt("callNumData", 0)
                DatabaseActions(context).addCallDetails(
                    CallDetails(
                        serialNumber,
                        phoneNumber!!,
                        SystemMethods().getContactName(phoneNumber, context),
                        SystemMethods().systemTime,
                        SystemMethods().systemDate
                    )
                )

                pref.edit().putInt("serialNumData", ++serialNumber).apply()
                pref.edit().putInt("callNumData", ++numofcalls).apply()


            }
            mFloatingView.cancelbtn.setOnClickListener {
                Toast.makeText(context, "recording stopped", Toast.LENGTH_SHORT).show()
                val recordStop = Intent(context, RecorderService::class.java)
                context.stopService(recordStop)
                mWindowManager.removeView(mFloatingView)
            }


        }
    }

 }
