package com.newpos.upos.customtext.test.NetworkStats;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.exercise.recy.DividerItemDecoration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "luo-FlowInfo";
    public static final String PACKAGE_NAME = "com.ums.tss.mastercontrol";
    public static final int GB = 1024 * 1024;
    public static final int MB = 1024;
    public static final int KB = 1;
    DecimalFormat df = new DecimalFormat("0.00");//格式化小数
    private Button totalMobile, totalWifi, singleMobile;
    private TextView totalMobileTv, totalWifiTv, singleMobileTv;
    private RecyclerView showSingleApp;
    private long totalMobileFlow, totalWifiFlow, signalMobileFlow;
    private List<SingleStatsBean> mList;
    private SingleStatsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_info);
        initView();
        initListener();
        setAction();
    }


    private void initView() {
        totalMobile = (Button) findViewById(R.id.total_mobile_btn);
        totalWifi = (Button) findViewById(R.id.total_wifi_btn);
//        singleMobile = (Button) findViewById(R.id.single_mobile_btn);
        totalMobileTv = (TextView) findViewById(R.id.total_mobile_tv);
        totalWifiTv = (TextView) findViewById(R.id.total_wifi_tv);
//        singleMobileTv = (TextView) findViewById(R.id.single_mobile_tv);
        showSingleApp = (RecyclerView) findViewById(R.id.single_app_recy);

    }

    private void initListener() {
        totalMobile.setOnClickListener(this);
        totalWifi.setOnClickListener(this);
//        singleMobile.setOnClickListener(this);
    }

    private void setAction() {
        initData();
        initViewData();
    }


    private void initData() {
        mList = new ArrayList<SingleStatsBean>();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            SingleStatsBean singleStats = new SingleStatsBean();
            singleStats.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());

            singleStats.setMobileConsume("");
            singleStats.setWifiConsume("总流量：" + getSingleBytesWifi(packageInfo.packageName));
//            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//            tmpInfo.packageName = packageInfo.packageName;
//            tmpInfo.versionName = packageInfo.versionName;
//            tmpInfo.versionCode = packageInfo.versionCode;
//            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
//            appList.add(tmpInfo);
            mList.add(singleStats);
        }
    }

    private void initViewData() {
        showSingleApp.setLayoutManager(new LinearLayoutManager(this));
        showSingleApp.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        Log.d(TAG, "initViewData: mList.size = " + mList.size());
        mAdapter = new SingleStatsAdapter(mList, FlowInfoActivity.this);
        showSingleApp.setAdapter(mAdapter);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.total_mobile_btn:
                long thisTimeMobile = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
                totalMobileFlow = thisTimeMobile + FlowInfoConstants.lastBootTotalMobile;
                Log.d(TAG, "onClick: ");
                if (totalMobileFlow / GB >= 1) {
                    totalMobileTv.setText(df.format((double) totalMobileFlow / GB) + "GB");
                } else if (totalWifiFlow / MB >= 1) {
                    totalMobileTv.setText(df.format((double) totalMobileFlow / MB) + "MB");
                } else {
                    totalMobileTv.setText(String.valueOf(totalMobileFlow / KB) + "KB");
                }
                break;
            case R.id.total_wifi_btn:
                long thisTimeWifi = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes() -
                        FlowInfoConstants.thisOpenTotalWifi;
                totalWifiFlow = thisTimeWifi + FlowInfoConstants.beforeTotalWifi;
                if (totalWifiFlow / GB >= 1) {
                    totalWifiTv.setText(df.format((double) totalWifiFlow / GB) + "GB");
                } else if (totalWifiFlow / MB >= 1) {
                    totalWifiTv.setText(df.format((double) totalWifiFlow / MB) + "MB");
                } else {
                    totalWifiTv.setText(String.valueOf(totalWifiFlow / KB) + "KB");
                }

                break;
//            case R.id.single_mobile_btn:
//                PackageManager pm = getPackageManager();
//                ApplicationInfo ai = null;
//                try {
//                    ai = pm.getApplicationInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Log.d(TAG, "onClick: UID IS:" + ai.uid);
//                signalMobileFlow = TrafficStats.getUidRxBytes(ai.uid);
//                singleMobileTv.setText(String.valueOf(signalMobileFlow) + "kb");
//                break;
        }
    }

    @SuppressLint("WrongConstant")
    public String getSingleBytesWifi(String packageName) {
        PackageManager pm = getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        signalMobileFlow = TrafficStats.getUidRxBytes(ai.uid) + TrafficStats.getUidTxBytes(ai.uid);
        if (signalMobileFlow == 0 || (TrafficStats.getUidRxBytes(ai.uid) == -1) && (TrafficStats.getUidTxBytes(ai.uid) == -1)) {
            signalMobileFlow = getTotalBytesManual(ai.uid);
        }
        Log.d(TAG, "onClick: UID IS:" + ai.uid);
        return String.valueOf(signalMobileFlow) + "KB";
    }

    /**
     * 通过uid查询文件夹中的数据
     * @param localUid
     * @return
     */
    private Long getTotalBytesManual(int localUid) {
//        Log.e("BytesManual*****", "localUid:" + localUid);
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < children.length; i++) {
            stringBuffer.append(children[i]);
            stringBuffer.append("   ");
        }
//        Log.e("children*****", children.length + "");
//        Log.e("children22*****", stringBuffer.toString());
        if (!Arrays.asList(children).contains(String.valueOf(localUid))) {
            return 0L;
        }
        File uidFileDir = new File("/proc/uid_stat/" + String.valueOf(localUid));
        File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
        File uidActualFileSent = new File(uidFileDir, "tcp_snd");
        String textReceived = "0";
        String textSent = "0";
        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
//                Log.e("receivedLine*****", "receivedLine:" + receivedLine);
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
//                Log.e("sentLine*****", "sentLine:" + sentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("IOException*****", e.toString());
        }
//        Log.e("BytesManualEnd*****", "localUid:" + localUid);
        return Long.valueOf(textReceived).longValue() + Long.valueOf(textSent).longValue();
    }
}
