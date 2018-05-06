package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import android.support.v7.app.NotificationCompat;

public class CancelNotificationService extends Service {

    public static int NOTIFICATION_ID = -1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getApplicationContext(), "CancelNotificationService.onCreate", Toast.LENGTH_SHORT).show();

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
        },1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
