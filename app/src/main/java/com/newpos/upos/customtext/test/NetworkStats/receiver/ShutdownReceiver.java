package com.newpos.upos.customtext.test.NetworkStats.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

import com.newpos.upos.customtext.test.NetworkStats.FlowInfoConstants;
import com.newpos.upos.customtext.test.NetworkStats.SPUtils;

public class ShutdownReceiver extends BroadcastReceiver {
    private static final String TAG = "luo-ShutdownReceiver";
    private long lastTotalMobile;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
            Log.i(TAG, "启动关闭中...");
            lastTotalMobile = (TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes()) / 1024;
            Log.d(TAG, "lastTotalMobile = " + lastTotalMobile + "Mb");
            SPUtils.put(context, FlowInfoConstants.LAST_TOATAL_MOBILE,lastTotalMobile);
        }
    }
}
