package com.newpos.upos.customtext.test.NetworkStats;

/**
 * Created by Wenson_Luo on 2017/12/15.
 */

public class SingleStatsBean {
    private String appName;
    private String mobileConsume;
    private String wifiConsume;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
        return "SingleStatsBean: appName = " + getAppName() + ",mobileConsume = " + getMobileConsume() +
                ",wifiConsume = " + getWifiConsume();
    }
}
