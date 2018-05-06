package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.app.Activity;
import android.content.*;
import android.os.IBinder;
import android.widget.Toast;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.spec.OAEPParameterSpec;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaKeepAliveMode extends CordovaPlugin {

    private static final String ACTION_METHOD_ENABLE = "enable";

    private static AlarmReceiver alarmReceiver = null;

    private static CordovaWebView webView = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.webView = webView;
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(cordova.getActivity(), "CordovaKeepAliveMode.onDestroy", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(cordova.getActivity(), BackgroundTaskService.class);
        cordova.getActivity().startService(serviceIntent);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }

        if (action.equals(ACTION_METHOD_ENABLE)) {
            this.enable(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void enable(JSONArray args, CallbackContext callbackContext) {
        if (args != null && args.length() > 0) {
            try {
                JSONObject opt = args.getJSONObject(0);

                /**
                 * 启动后台任务service进程
                 */
                Intent serviceIntent = new Intent(cordova.getActivity(), BackgroundTaskService.class);
                /*cordova.getActivity().bindService(serviceIntent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                }, cordova.getActivity().BIND_AUTO_CREATE);*/

                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    cordova.getActivity().startForegroundService(serviceIntent);
                } else {
                    cordova.getActivity().startService(serviceIntent);
                }

                alarmReceiver = new AlarmReceiver();
                alarmReceiver.setCallbackContext(callbackContext);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_ON);
                intentFilter.addAction(AlarmReceiver.ACTION_ALARM_RUN);
                intentFilter.addAction(AlarmReceiver.ACTION_ALARM_START);
                intentFilter.addAction(AlarmReceiver.ACTION_ALARM_STOP);
                cordova.getActivity().registerReceiver(alarmReceiver, intentFilter);

                Intent intent = new Intent();
                intent.setAction(AlarmReceiver.ACTION_ALARM_START);
                intent.putExtra("opt", opt.toString());
                cordova.getActivity().sendBroadcast(intent);

                callbackContext.success(opt);
            } catch (Exception e) {
                e.printStackTrace();
                callbackContext.error("json 解析出错");
            }
        }
    }

    public static void fireEvent (String event, String result) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>fireEvent>>>>>>>>>>>>>>>." + event + ">>>>>>>>>" + result);
        String params = "\"" + result + "\"";

        String js = "cordova.plugins.CordovaKeepAliveMode.fireEvent(" +
                "\"" + event + "\"," + params + ")";

        sendJavascript(js);
    }

    /**
     * 执行js代码
     */
    private static synchronized void sendJavascript(final String js) {

        Runnable jsLoader = new Runnable() {
            public void run() {
                webView.loadUrl("javascript:" + js);
            }
        };
        try {
            Method post = webView.getClass().getMethod("post",Runnable.class);
            post.invoke(webView,jsLoader);
        } catch(Exception e) {

            ((Activity)(webView.getContext())).runOnUiThread(jsLoader);
        }
    }
}
