package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s-guanhm on 2018/5/7.
 */
public class DaemonActivity extends Activity {
    public static final String CLOSE_ACTION = "close";

    private Context aContext = DaemonActivity.this;

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

        View view = new View(getApplicationContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });

        setContentView(view);


    }
}
