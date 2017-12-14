package com.newpos.upos.customtext.exercise;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wenson_Luo on 2017/8/22.
 * 拿 SimInfo 信息
 */

public class TowerInfoActivity extends AppCompatActivity {
    private static final String TAG = "TowerInfoActivity";

    private Button btnTowerInfo;
    private TextView showMessage;
    private MyPhoneStateListener myListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower);
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(TAG, "onCreate: --------SDK_VERSION>=23");
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.READ_SMS}, 1);
            }
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        btnTowerInfo = (Button) findViewById(R.id.btn_tower);
        showMessage = (TextView) findViewById(R.id.tv_show);
//        myListener = new MyPhoneStateListener();
        btnTowerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTowerInfo1(getBaseContext());
//                getSimInfo();
//                List<String> list = getTowerInfo1(getBaseContext());
//                showMessage.append("\n"+list.toString());
            }
        });
    }

    /**
     * Android 基站分CdmaCellLocation和GsmCellLocation，要根据不同的SIM卡转成不同的对象。在中国，
     * 移动的2G是EGDE，联通的2G为GPRS，电信的2G为CDMA，电信的3G为EVDO
     */
    private int strength;
    public void getTowerInfo() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(TAG, "onCreate: --------SDK_VERSION>=23");
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.READ_SMS}, 1);
            }
            if (ContextCompat.checkSelfPermission(TowerInfoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TowerInfoActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation cellLocation = tm.getCellLocation();
        if (cellLocation instanceof CdmaCellLocation) {//中国电信
            CdmaCellLocation location = (CdmaCellLocation)cellLocation;
            LoggerUtils.i("----------CdmaCellLocation = " + location);
            if (location == null){
                Log.e(TAG, "getTowerInfo: error");
                return;
            }
            int cellId = location.getBaseStationId();
            int lac = location.getNetworkId();

            String operator = tm.getNetworkOperator();

            int mnc = location.getSystemId();
            int mcc = Integer.parseInt(operator.substring(0, 3));
            StringBuilder sb = new StringBuilder();

            sb.append("\nmcc = " + mcc);
            sb.append("\nmnc = " + mnc);
            sb.append("\nlac = " + lac);
            sb.append("\ncellId = " + cellId);

            Log.i(TAG, sb.toString());
            showMessage.setText(sb.toString());
        } else if (cellLocation instanceof GsmCellLocation){
            GsmCellLocation location = (GsmCellLocation) cellLocation;
            Log.i(TAG, "-----------GsmCellLocation = " + location);
            if (location == null){
                Log.e(TAG, "getTowerInfo: error");
                return;
            }
            int cellId = location.getCid();
            int lac = location.getLac();
            String operator = tm.getNetworkOperator();
            Log.i(TAG, "getTowerInfo: -------NetworkOperator = " + operator);
            int mcc = Integer.parseInt(operator.substring(0, 3));
            int mnc = Integer.parseInt(operator.substring(3));

            StringBuilder sb = new StringBuilder();

            sb.append("\nmcc = " + mcc);
            sb.append("\nmnc = " + mnc);
            sb.append("\nlac = " + lac);
            sb.append("\ncellId = " + cellId);
            Log.i(TAG, sb.toString());
            showMessage.setText(sb.toString());
        }

        CellInfo info = tm.getAllCellInfo().get(0);
        LoggerUtils.i("----------info = " + info);
        if (info instanceof CellInfoCdma){
            CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
            strength = cellSignalStrengthCdma.getCdmaDbm();
        }else if (info instanceof CellInfoGsm){
            CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
            strength = cellSignalStrengthGsm.getDbm();
        }else if (info instanceof CellInfoLte){
            CellInfoLte cellInfoLte = (CellInfoLte) info;
            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
            strength = cellSignalStrengthLte.getDbm();
        }else if (info instanceof CellInfoWcdma){
            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
            CellSignalStrengthWcdma cellSignalStrengthWcdma = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                strength = cellSignalStrengthWcdma.getDbm();
            }
        }else {
            LoggerUtils.e("getSimOperator error");
            return;
        }
        Log.i(TAG, "getTowerInfo: -------获取信号强度:" + strength);
        showMessage.append("\n获取信号强度:"+strength);

//        List<CellInfo> infos = tm.getAllCellInfo();
//        StringBuffer sb = new StringBuffer("总数：" + infos.size() + "\n");
//        for (CellInfo info : infos){
//            CellInfoLte cellInfoLte = (CellInfoLte) info;
//            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
//            int strength = cellSignalStrengthLte.getDbm();
//            sb.append("strength : " + strength);
//        }
//        Log.i(TAG, "getTowerInfo: -------获取所有基站信息:" + sb.toString());
//        showMessage.append("\n获取所有基站信息:"+sb.toString());

    }

    public void getSimInfo() {
        String ret = null;
        try {

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            StringBuilder sb = new StringBuilder();

            sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
            sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
            sb.append("\nLine1Number = " + tm.getLine1Number());
            sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
            sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
            sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
            sb.append("\nNetworkType = " + tm.getNetworkType());
            sb.append("\nPhoneType = " + tm.getPhoneType());
            sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
            sb.append("\nSimOperator = " + tm.getSimOperator());
            sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
            sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
            sb.append("\nSimState = " + tm.getSimState());
            sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
            sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
            Log.i(TAG, sb.toString());
            showMessage.append(sb.toString());
        } catch (Exception e) {
            LoggerUtils.e("---------" + e.getMessage());
        }
    }


    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            Log.i(TAG, "onSignalStrengthsChanged: --------GSM signalStrength = "
                + String.valueOf(signalStrength.getGsmSignalStrength()) + ", CDMA signalStrength = "+
                    String.valueOf(signalStrength.getCdmaDbm()));
            showMessage.append("\n"+String.valueOf(signalStrength.getGsmSignalStrength()));
        }
    }

//    运营商代码460开头的整理：
//            46000 中国移动 （GSM）
//            46001 中国联通 （GSM）
//            46002 中国移动 （TD-S）
//            46003 中国电信（CDMA）
//            46004 空（似乎是专门用来做测试的）
//            46005 中国电信 （CDMA）
//            46006 中国联通 （WCDMA）
//            46007 中国移动 （TD-S）
//            46008
//            46009
//            46010
//            46011 中国电信 （FDD-LTE）

    private int mcc = -1;
    private int mnc = -1;
    private int lac = -1;
    private int cellId = -1;
    private int rssi = -1;
    //获取基站信息
    public void getTowerInfo1(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getNetworkOperator();
        mcc = Integer.parseInt(operator.substring(0, 3));
        List<String> list = new ArrayList<String>(3);
        List<CellInfo> infos = tm.getAllCellInfo();
        for (CellInfo info : infos){
            if (info instanceof CellInfoCdma){
                CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                mnc = cellIdentityCdma.getSystemId();
                lac = cellIdentityCdma.getNetworkId();
                cellId = cellIdentityCdma.getBasestationId();
                CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                rssi = cellSignalStrengthCdma.getCdmaDbm();
            }else if (info instanceof CellInfoGsm){
                CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                mnc = cellIdentityGsm.getMnc();
                lac = cellIdentityGsm.getLac();
                cellId = cellIdentityGsm.getCid();
                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                rssi = cellSignalStrengthGsm.getDbm();
            }else if (info instanceof CellInfoLte){
                CellInfoLte cellInfoLte = (CellInfoLte) info;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                mnc = cellIdentityLte.getMnc();
                lac = cellIdentityLte.getTac();
                cellId = cellIdentityLte.getCi();
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                rssi = cellSignalStrengthLte.getDbm();
            }else if (info instanceof CellInfoWcdma){
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
                CellIdentityWcdma cellIdentityWcdma = null;
                CellSignalStrengthWcdma cellSignalStrengthWcdma = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    mnc = cellIdentityWcdma.getMnc();
                    lac = cellIdentityWcdma.getLac();
                    cellId = cellIdentityWcdma.getCid();
                    cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                    rssi = cellSignalStrengthWcdma.getDbm();
                }
            }else {
                LoggerUtils.e("get CellInfo error");
                return;
            }
            String tower = String.valueOf(mcc) + "#" + String.valueOf(mnc) + "#" + String.valueOf(lac)
                    + "#" + String.valueOf(cellId) + "#" + String.valueOf(rssi);
            list.add(tower);
        }
        if (list.size() > 6){
            list = list.subList(0, 5);
        }else if (list.size() < 3){
            int need = 3 - list.size();
            for (int i = 0; i < need; i++) {
                list.add("");
            }
        }
        showMessage.append(list.toString());
    }

}
