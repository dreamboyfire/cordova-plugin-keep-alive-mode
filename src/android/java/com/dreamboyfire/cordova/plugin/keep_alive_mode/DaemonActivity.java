package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.view.*;
import android.widget.Toast;

/**
 * Created by s-guanhm on 2018/5/7.
 */
public class DaemonActivity extends Activity {
    public static final String CLOSE_ACTION = "close";

    private BroadcastReceiver finishReceiver = null;

    private static Intent newIntent(Context context) {
        Intent intent = new Intent(context, DaemonActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static void startActivity(Context context) {

        if (!mStart) {
            context.startActivity(newIntent(context));
        }
    }


    public static boolean mStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStart = true;

        Toast.makeText(this, "DaemonActivity.onCreate", Toast.LENGTH_SHORT).show();

        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        finishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        registerReceiver(finishReceiver, new IntentFilter("FINISH_ACTIVITY"));


        checkScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "DaemonActivity.onDestroy", Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {

        PowerManager pm = (PowerManager) DaemonActivity.this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            finish();
        }
    }


}
