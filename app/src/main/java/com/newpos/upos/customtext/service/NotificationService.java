package com.newpos.upos.customtext.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.newpos.upos.customtext.exercise.OneActivity;

/**
 * Created by Wenson_Luo on 2017/8/8.
 */

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, OneActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new Notification();
        notification.contentIntent = pendingIntent;
        notification.tickerText = "您有一条新的信息";
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.when = 10L;
    }

    public class MyBinder extends Binder{

    }
}
