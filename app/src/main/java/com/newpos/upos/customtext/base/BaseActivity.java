package com.newpos.upos.customtext.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Wenson_Luo on 2017/7/22.
 */

public class BaseActivity extends AppCompatActivity {


    public void setTitleName(final String name, final int id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(id)).setText(name);
            }
        });
    }

//    public void onAttachedToWindow()
//    {
//        super.onAttachedToWindow();
//        Window localWindow = getWindow();//拿到当前activity 的 window
//        try
//        {
//            Method localMethod = localWindow.getClass().getMethod("addCustomFlags", new Class[] { Integer.TYPE });
//            localMethod.setAccessible(true);
//            localMethod.invoke(localWindow, new Object[] { Integer.valueOf(1) });
//            return;
//        }
//        catch (Exception localException)
//        {
//            localException.printStackTrace();
//        }
//    }
}
