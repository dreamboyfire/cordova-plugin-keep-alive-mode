package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaKeepAliveMode extends CordovaPlugin {

    private static final String ACTION_METHOD_ENABLE = "enable";

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
                cordova.getActivity().bindService(serviceIntent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                }, cordova.getActivity().BIND_AUTO_CREATE);

//                cordova.getActivity().startService(serviceIntent);

                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    cordova.getActivity().startForegroundService(serviceIntent);
                } else {
                    cordova.getActivity().startService(serviceIntent);
                }

                Intent intent = new Intent(cordova.getActivity(), AlarmReceiver.class);
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
}
