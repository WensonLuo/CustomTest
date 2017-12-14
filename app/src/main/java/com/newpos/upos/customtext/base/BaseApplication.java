package com.newpos.upos.customtext.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.newpos.loghelper.Entry;

/**
 * Created by Wenson_Luo on 2017/7/22.
 */

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";

    static Context _context;
    static Resources _resource;

    private static boolean isAtLeastKIT;//API 19

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            isAtLeastKIT = true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ----------");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: -----------");

    }
}
