package com.newpos.upos.customtext.test.NetworkStats.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.newpos.upos.customtext.test.NetworkStats.FlowInfoConstants;
import com.newpos.upos.customtext.test.NetworkStats.SPUtils;

public class WiFiStateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "luo-WiFiStateChangeReceiver";
    private long openTotalWifi;
    private long closeTotalWifi;
    private long thisTimeWifi;//本次开关wifi期间所消耗的流量
    private long beforeWifi;//本次开wifi之前，所消耗的wifi流量

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")){
            //获取当前的wifi状态int类型数据
            Log.e(TAG,"wifi状态发生变化了");
            int mWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (mWifiState ) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.e(TAG,"wifi状态发生变化了=打开");
                    openTotalWifi = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                    FlowInfoConstants.thisOpenTotalWifi = openTotalWifi;
                    Log.d(TAG, "openTotalWifi = " + openTotalWifi);
                    beforeWifi = (long) SPUtils.get(context, FlowInfoConstants.BEFORE_TOTAL_WIFI,0);
                    Log.d(TAG, "beforeWifi = " + beforeWifi + ", thisTimeWifi = " + thisTimeWifi);
                    beforeWifi += thisTimeWifi;
                    Log.d(TAG, "after add, beforeWifi = " + beforeWifi);
                    FlowInfoConstants.beforeTotalWifi = beforeWifi;//赋值给公共变量
                    SPUtils.put(context,FlowInfoConstants.BEFORE_TOTAL_WIFI,beforeWifi);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    //打开中
                    Log.e(TAG,"wifi状态发生变化了=打开中");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    //已关闭
                    Log.e(TAG,"wifi状态发生变化了=已关闭");
                    closeTotalWifi = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                    thisTimeWifi = closeTotalWifi - openTotalWifi;
                    Log.d(TAG, "closeTotalWifi = " + closeTotalWifi + ",thisTimeWifi = " + thisTimeWifi);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.e(TAG,"wifi状态发生变化了=关闭中");
                    //关闭中
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    //未知
                    break;
            }
        }
    }
}
