package com.example.ahoyandroidtaskmudassar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat



class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            Log.e("ALARMValues", "ALARM RUNNING" + intent.flags)
           /* val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.old_telephone)
            mediaPlayer.start()*/
            val name = intent.getStringExtra("PLAYERNAME")
            val id = intent.getIntExtra("EVENTID", 0)
            val notificationHelper = NotificationHelper(context!!, name!!, id)
            val nb: NotificationCompat.Builder = notificationHelper.channelNotification
            notificationHelper.manager!!.notify(id, nb.build())
        }
    }
}