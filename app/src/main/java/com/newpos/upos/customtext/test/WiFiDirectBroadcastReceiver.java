package com.newpos.upos.customtext.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by ydc on 17-8-29.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver implements WifiP2pManager.PeerListListener {
    private static final String TAG = "luo---WiFiDirectBroadcastRece";
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private RegisterActivity mActivity;

    public WiFiDirectBroadcastReceiver() {
    }

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       RegisterActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("WifiProbeService","onReceive"+action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // Check to see if Wi-Fi is enabled and notify appropriate activity
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            //int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
        Log.i("WifiProbeService","WIFI_P2P_STATE_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            Log.i("WifiProbeService","WIFI_P2P_PEERS_CHANGED_ACTION");
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, this);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            Log.i("WifiProbeService","WIFI_P2P_CONNECTION_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            Log.i("WifiProbeService","WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }else  if(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED) {
                Log.i("WifiProbeService","WIFI_P2P_DISCOVERY_CHANGED_ACTION WIFI_P2P_DISCOVERY_STOPPED->WIFI_P2P_DISCOVERY_START");
                mActivity.discovery();
            }
          //  mActivity.discovery();

        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }
}
