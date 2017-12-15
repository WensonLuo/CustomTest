package com.newpos.upos.customtext.test.NetworkStats;

/**
 * Created by Wenson_Luo on 2017/12/15.
 */

public class FlowInfoConstants {
    public static final String LAST_TOATAL_MOBILE = "lastTotalMobile";//存sp的key,上次开机存的移动流量
    public static final String BEFORE_TOTAL_WIFI = "beforeTotalWifi";//本次开启wifi之前所存储的wifi流量
    public static long lastBootTotalMobile;//上次关机时的总移动数据流量
    public static long thisOpenTotalWifi;//本次打开wifi时取到的总流量
    public static long beforeTotalWifi;//之前存储的wifi流量
}
