package com.dreamboyfire.cordova.plugin.keep_alive_mode;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class NetworkService extends GcmTaskService {
    @Override
    public int onRunTask(TaskParams taskParams) {
        return 0;
    }
}
