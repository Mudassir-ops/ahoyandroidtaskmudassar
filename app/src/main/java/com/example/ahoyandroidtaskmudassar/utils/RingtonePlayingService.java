package com.example.ahoyandroidtaskmudassar.utils;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service {
    private static final String TAG = RingtonePlayingService.class.getSimpleName();
    private static final String URI_BASE = RingtonePlayingService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";
    public static final String ALARM_ID = "Alarm_Id";
    int AlarmId;

    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommandHere" + intent);
      //  NotificationHelper.ringtone.stop();
//        if (NotificationHelper.ringtone.isPlaying()) {
//            Toast.makeText(this, "music is playing", Toast.LENGTH_LONG).show();
//            NotificationHelper.ringtone.stop();
//        }
        if (intent == null) {
            Log.d(TAG, "The intent is null.");
            return START_REDELIVER_INTENT;
        }

        String action = intent.getAction();
        AlarmId = intent.getIntExtra(ALARM_ID, 0);
        dismissRingtone();

     /*   if ("YES_ACTION".equals(action)) {
            Toast.makeText(this, "YES CALLED", Toast.LENGTH_SHORT).show();
        }
        if (ACTION_DISMISS.equals(action))
            dismissRingtone();
        else {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();
        }*/

        return START_NOT_STICKY;
    }

    public void dismissRingtone() {
        // stop the alarm rigntone
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);

        // also dismiss the alarm to ring again or trigger again
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        aManager.cancel(pendingIntent);
       // NotificationHelper.ringtone.stop();
        NotificationHelper.v.cancel();

        // Canceling the current notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmId);
    }

    @Override
    public void onDestroy() {

    }
}