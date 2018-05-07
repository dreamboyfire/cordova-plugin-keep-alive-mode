package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by s-guanhm on 2018/5/3.
 */
public class BackgroundTaskService extends Service {

    public static int NOTIFICATION_ID = 64323;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        NOTIFICATION_ID = startId;

        Toast.makeText(getApplicationContext(), "onStartCommand", Toast.LENGTH_SHORT).show();

        /*AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent testIntent = new Intent(getApplicationContext(), BackgroundTaskService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, testIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);*/

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//        Notification notification = builder.build();
//
        startForeground(NOTIFICATION_ID, new Notification());
        startService(new Intent(this, InnerService.class));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class InnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            startForeground(NOTIFICATION_ID, new Notification());
            stopSelf();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }
    }


}
