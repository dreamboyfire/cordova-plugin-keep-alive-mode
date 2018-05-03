package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {

    public static String ACTION_ALARM_START = "ACTION_ALARM_START";
    public static String ACTION_ALARM_RUN = "ACTION_ALARM_RUN";
    public static String ACTION_ALARM_STOP = "ACTION_ALARM_STOP";

    public static boolean isStop = true;

    public static JSONObject option = null;

    private PendingIntent pendingIntent = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_ALARM_START)) {
            isStop = false;
            option = (JSONObject) JSON.parse(intent.getStringExtra("opt"));

            setAlarm(context);
        }

        if (intent.getAction().equals(ACTION_ALARM_RUN)) {
            try {
                Toast.makeText(context, option.getString("toastTips"), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*if (isStop) {
                if (pendingIntent != null) {
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.cancel(pendingIntent);
                    pendingIntent = null;
                }
            } else {
                setAlarm(context);
            }*/

            setAlarm(context);
        }

//        Intent serviceIntent = new Intent(context, BackgroundTaskService.class);
//        context.startService(serviceIntent);


        if (intent.getAction().equals(ACTION_ALARM_STOP)) {
            isStop = true;
            if (pendingIntent != null) {
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.cancel(pendingIntent);
                pendingIntent = null;
            }
        }

    }

    private void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_ALARM_RUN);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmManager.AlarmClockInfo info = null;
            try {
                info = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + option.getIntValue("time"), pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            am.setAlarmClock(info, pendingIntent);
        }
    }
}
