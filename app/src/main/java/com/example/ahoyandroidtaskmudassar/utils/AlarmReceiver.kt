package com.example.ahoyandroidtaskmudassar.utils

import android.content.BroadcastReceiver
import android.content.Intent

import android.content.Context

import android.util.Log
import androidx.core.app.NotificationCompat

import com.example.ahoyandroidtaskmudassar.utils.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent != null) {
            Log.e("ALARMValues", "ALARM RUNNING" + intent.flags)
            /* val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.old_telephone)
             mediaPlayer.start()*/
            val name = intent.getStringExtra("CITYNAME")
            val id = intent.getIntExtra("EVENTID", 0)
            val temp = intent.getStringExtra("TEMP")
            val notificationHelper = NotificationHelper(context, name, temp,id)
            val nb: NotificationCompat.Builder = notificationHelper.channelNotification
            notificationHelper.manager.notify(id, nb.build())
        }
    }
}
