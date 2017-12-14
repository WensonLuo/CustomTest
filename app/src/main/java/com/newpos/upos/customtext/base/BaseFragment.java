package com.newpos.upos.customtext.base;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.newpos.upos.customtext.utils.LoggerUtils;

/**
 * Created by Wenson_Luo on 2017/7/22.
 */

public class BaseFragment extends Fragment {

    private Handler handler = new Handler();

    public BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }

    @Override
    public void onStart() {
        LoggerUtils.d("enter.");
        super.onStart();
    }

    /**
     * Get handler in the ui thread
     * @return
     */
    public Handler getUIHandler(){
        return handler;
    }

    public static <T extends BaseFragment> T getInstance(Class<T> tClass){
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

}
