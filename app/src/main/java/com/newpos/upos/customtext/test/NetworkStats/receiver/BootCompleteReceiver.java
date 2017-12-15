package com.newpos.upos.customtext.test.NetworkStats.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "luo-BootComplete";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            //开机启动wifi探针服务
            Log.d(TAG, "开机启动中");
        }
    }
}
