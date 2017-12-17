package com.newpos.upos.customtext.test.NetworkStats;

import android.content.pm.ApplicationInfo;

/**
 * Created by Wenson_Luo on 2017/12/15.
 */

public class SingleStatsModel {
    private ApplicationInfo appInfo;
    private String mobileConsume;
    private String wifiConsume;

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public String getMobileConsume() {
        return mobileConsume;
    }

    public void setMobileConsume(String mobileConsume) {
        this.mobileConsume = mobileConsume;
    }

    public String getWifiConsume() {
        return wifiConsume;
    }

    public void setWifiConsume(String wifiConsume) {
        this.wifiConsume = wifiConsume;
    }

    @Override
    public String toString() {
        return "SingleStatsModel: appName = " + appInfo.name + ",mobileConsume = " + getMobileConsume() +
                ",wifiConsume = " + getWifiConsume();
    }
}
