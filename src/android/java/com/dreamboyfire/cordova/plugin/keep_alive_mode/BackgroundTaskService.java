package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by s-guanhm on 2018/5/3.
 */
public class BackgroundTaskService extends Service {

    public static int NOTIFICATION_ID = -1;

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

        NOTIFICATION_ID = startId;

        Toast.makeText(getApplicationContext(), "onStartCommand", Toast.LENGTH_SHORT).show();

        /*AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent testIntent = new Intent(getApplicationContext(), BackgroundTaskService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, testIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);*/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification = builder.build();

        startForeground(NOTIFICATION_ID, notification);

        Intent innerIntent = new Intent(this, InnerService.class);
        startService(innerIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class InnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            startForeground(NOTIFICATION_ID, builder.build());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Cancel Notification!", Toast.LENGTH_SHORT).show();
                    stopForeground(true);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFICATION_ID);
                    stopSelf();
                }
            },100);

        }
    }
}
