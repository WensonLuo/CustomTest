package com.newpos.upos.customtext.test;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.newpos.upos.customtext.R;

public class FlowInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "luo-FlowInfo";
    public static final String PACKAGE_NAME = "com.ums.tss.mastercontrol";
    private Button totalMobile, totalWifi, singleMobile;
    private TextView totalMobileTv, totalWifiTv, singleMobileTv;
    private long totalMobileFlow, totalWifiFlow, signalMobileFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_info);
        initView();
        initListener();
    }

    private void initView() {
        totalMobile = (Button) findViewById(R.id.total_mobile_btn);
        totalWifi = (Button) findViewById(R.id.total_wifi_btn);
        singleMobile = (Button) findViewById(R.id.single_mobile_btn);
        totalMobileTv = (TextView) findViewById(R.id.total_mobile_tv);
        totalWifiTv = (TextView) findViewById(R.id.total_wifi_tv);
        singleMobileTv = (TextView) findViewById(R.id.single_mobile_tv);
    }

    private void initListener() {
        totalMobile.setOnClickListener(this);
        totalWifi.setOnClickListener(this);
        singleMobile.setOnClickListener(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.total_mobile_btn:
                totalMobileFlow = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
                totalMobileTv.setText(String.valueOf(totalMobileFlow) + "kb");
                break;
            case R.id.total_wifi_btn:
                totalWifiFlow = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes() -
                        totalMobileFlow;
                totalWifiTv.setText(String.valueOf(totalWifiFlow) + "kb");
                break;
            case R.id.single_mobile_btn:
                PackageManager pm = getPackageManager();
                ApplicationInfo ai = null;
                try {
                    ai = pm.getApplicationInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onClick: UID IS:" + ai.uid);
                signalMobileFlow = TrafficStats.getUidRxBytes(ai.uid);
                singleMobileTv.setText(String.valueOf(signalMobileFlow) + "kb");
                break;
        }
    }
}
