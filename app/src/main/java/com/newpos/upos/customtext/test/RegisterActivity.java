package com.newpos.upos.customtext.test;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.utils.LoggerUtils;

/**
 * 测一下动态注册广播，没问题，是这样写
 * Created by Wenson_Luo on 2017/9/26.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "luo---RegisterActivity";
    public static final String TEST_ACTION = "test.action";
    public static RegisterActivity activity = new RegisterActivity();
    private Button registerBtn;
    private Button unregisterBtn;
    private Button sendBroadcastBtn;

    private WiFiDirectBroadcastReceiver mReceiver;
    private WifiManager mWifiManager;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private boolean registerTag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = (Button) findViewById(R.id.register);
        unregisterBtn = (Button) findViewById(R.id.unregister);
        sendBroadcastBtn = (Button) findViewById(R.id.send_broadcast);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerTag){
                    Log.i(TAG,"---register Tag = " + registerTag + ",mReceiver = " + mReceiver.toString());
                    registerTag = false;
                    unregisterReceiver(mReceiver);
                }
                IntentFilter mIntentFilter = new IntentFilter();
                mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
                mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
                mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                if (mWifiP2pManager != null) {
                    mChannel = mWifiP2pManager.initialize(getBaseContext(), getMainLooper(), null);
                    if (mChannel == null) {
                        //Failure to set up connection
                        Log.e(TAG, "Failed to set up connection with wifi p2p service");
                        mWifiP2pManager = null;
                    }
                } else {
                    Log.e(TAG, "mWifiP2pManager is null !");
                }
                mReceiver = new WiFiDirectBroadcastReceiver(mWifiP2pManager, mChannel, activity);
                Log.i(TAG,"---register Tag = " + registerTag + ",mReceiver = " + mReceiver.toString());
                if (!registerTag){
                    registerTag = true;
                    registerReceiver(mReceiver,mIntentFilter);
                }
                discovery();
            }
        });
        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG,"unregister Tag----" + registerTag+ ",mReceiver = " + mReceiver.toString());
                if (registerTag){
                    registerTag = false;
                    unregisterReceiver(mReceiver);
                }
            }
        });
        sendBroadcastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerTag){
                    LoggerUtils.d("send Broadcast");
                    sendBroadcast(new Intent(TEST_ACTION));
                }
            }
        });

    }


    // discoveryPeer a listener
    public void discovery() {
        if (mChannel == null) {
            return;
        }
        if (mWifiP2pManager != null) {
            mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "onSuccess onSuccess !");
                }

                @Override
                public void onFailure(int reasonCode) {
                    Log.e(TAG, "onFailure onFailure onFailure !");
                }
            });
        }
    }

//    private class MyBroadcast extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LoggerUtils.i("----------onReceive");
//        }
//    }
}
