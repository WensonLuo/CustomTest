package com.newpos.upos.customtext.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newpos.upos.customtext.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 1.需要添加权限，开关切换不能太快，需要间隔几秒
 * 2.机器内需要插有SIM卡，才能开关设置内的移动数据按钮
 *    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 *    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
 */
public class NetStateChangeActivity extends AppCompatActivity {
    private static final String TAG = "NetStateChangeActivity";
    private boolean switchFlag;
    Button switchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_service);
        Log.d(TAG, "onCreate:switchFlag =  " + switchFlag);
        switchBtn = (Button) findViewById(R.id.switch_btn);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFlag = getMobileDataState(NetStateChangeActivity.this);
                Log.d(TAG, "onClick:switchFlag =  " + switchFlag);
                if (switchFlag) {
                    setMobileDataState(NetStateChangeActivity.this, false);
                    Toast.makeText(NetStateChangeActivity.this, "关闭移动数据", Toast.LENGTH_SHORT).show();

                } else {
                    setMobileDataState(NetStateChangeActivity.this,true);
                    Toast.makeText(NetStateChangeActivity.this, "打开移动数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //打开或者关闭移动数据
    public void setMobileDataState(Context cxt, boolean mobileDataEnabled) {
        TelephonyManager telephonyService = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception e) {
            Log.v(TAG, "Error setting" + ((InvocationTargetException)e).getTargetException() + telephonyService);
        }
    }

    //获取移动数据的开关状态
    public boolean getMobileDataState(Context cxt) {
        TelephonyManager telephonyService = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
                return mobileDataEnabled;
            }
        }
        catch (Exception e) {
            Log.v(TAG, "Error getting" + ((InvocationTargetException)e).getTargetException() + telephonyService);
        }

        return false;
    }
}
