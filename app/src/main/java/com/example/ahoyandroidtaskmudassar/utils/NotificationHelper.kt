package com.example.ahoyandroidtaskmudassar.utils

import android.content.ContextWrapper
import android.net.Uri
import android.content.ContentResolver
import com.example.ahoyandroidtaskmudassar.R
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import com.example.ahoyandroidtaskmudassar.utils.NotificationHelper
import android.os.Vibrator
import android.media.RingtoneManager
import android.os.VibrationEffect
import java.lang.Exception
import android.media.Ringtone
import android.content.Intent
import com.example.ahoyandroidtaskmudassar.utils.RingtonePlayingService
import android.app.PendingIntent
import android.content.Context

class NotificationHelper(base: Context, name: String, Id: Int) : ContextWrapper(base) {
    var soundUri =
        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.old_telephone)
    var actionYes: NotificationCompat.Action
    private var name = ""
    private var id = 0
    private var mManager: NotificationManager? = null
    var sound: Uri? = null
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
        val pattern = longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100)
        try {
            val notification =
                Uri.parse("android.resource://" + packageName + "/" + R.raw.old_telephone)
            v = getSystemService(VIBRATOR_SERVICE) as Vibrator
            ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            v!!.vibrate(VibrationEffect.createWaveform(pattern, 3))
            ringtone.play()
            //   v.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    /*  return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setVibrate(new long[]{50, 100, 150, 200, 250})
                .setContentTitle(name)
                .setContentText("Alarm")
                .addAction(actionYes)
                .setSmallIcon(R.drawable.image);

    }*/
    val channelNotification: NotificationCompat.Builder
        get() {
            val mBuilder = NotificationCompat.Builder(applicationContext, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText("Alarm")
                .addAction(actionYes)
                .setSmallIcon(R.mipmap.ic_launcher)
            mBuilder.setVibrate(longArrayOf(500, 500))
            val mNotifyMgr = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(id, mBuilder.build())
            return mBuilder
        }

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
        var r: Ringtone? = null
        lateinit var ringtone: Ringtone
        var v: Vibrator? = null
    }

    init {
        this.name = name
        id = Id
        sound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + base.packageName + "/" + R.raw.old_telephone)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val yesIntent = Intent(this, RingtonePlayingService::class.java)
        yesIntent.putExtra(RingtonePlayingService.ALARM_ID, id)
        val pendingIntentYes =
            PendingIntent.getService(this, id, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        actionYes = NotificationCompat.Action(
            android.R.drawable.ic_lock_idle_alarm,
            "Dismiss",
            pendingIntentYes
        )
    }
}