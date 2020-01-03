package com.example.mycallrecorder

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import java.io.IOException


class RecorderService : Service() {
    var recorder: MediaRecorder? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        recorder = MediaRecorder()
        recorder!!.reset()
        val phoneNumber = intent.getStringExtra("number")
        val time: String = SystemMethods().systemTime
        val path: String = SystemMethods().systemPath
        val rec = path + "/" + phoneNumber + "_" + time + ".mp4"
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        recorder!!.setOutputFile(rec)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        recorder!!.start()

        return START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        recorder!!.stop()
        recorder!!.reset()
        recorder!!.release()
        recorder = null

    }

}