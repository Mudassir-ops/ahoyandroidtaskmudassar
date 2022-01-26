package com.example.ahoyandroidtaskmudassar.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.ahoyandroidtaskmudassar.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.old_telephone);
    public static Ringtone r = null;
    public static Ringtone ringtone = null;
    public static Vibrator v = null;
    NotificationCompat.Action actionYes;

    private String name = "";
    private String temp = "";
    private int id = 0;
    private NotificationManager mManager;
    Uri sound = null;

    public NotificationHelper(Context base, String name,String temp, int Id) {
        super(base);
        this.name = name;
        this.temp = temp;
        this.id = Id;
        sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + base.getPackageName() + "/" + R.raw.old_telephone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        Intent yesIntent = new Intent(this, RingtonePlayingService.class);
        yesIntent.putExtra(RingtonePlayingService.ALARM_ID, id);
        PendingIntent pendingIntentYes = PendingIntent.getService(this, id, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        actionYes = new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm, "Dismiss", pendingIntentYes);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);

        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
        try {
            Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.old_telephone);
            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            //ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            v.vibrate(VibrationEffect.createWaveform(pattern, 3));
          //  ringtone.play();
            //   v.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(name)
                        .setContentText(temp)
                        .addAction(actionYes)
                        .setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setVibrate(new long[]{500, 500});
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

        mNotifyMgr.notify(id, mBuilder.build());
        return mBuilder;
    }
}
      /*  return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setVibrate(new long[]{50, 100, 150, 200, 250})
                .setContentTitle(name)
                .setContentText("Alarm")
                .addAction(actionYes)
                .setSmallIcon(R.drawable.image);

    }*/

