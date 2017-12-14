package com.newpos.upos.customtext.exercise;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newpos.loghelper.Entry;
import com.newpos.loghelper.bean.LogConfigInfo;
import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.base.BaseActivity;
import com.newpos.upos.customtext.exercise.parcel.BasicInfo;
import com.newpos.upos.customtext.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 那logCatcher配置信息
 * Created by Wenson_Luo on 2017/7/22.
 */

public class OneActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OneActivity";
    private ArrayList<BasicInfo> info;
    private Button btnOne;
    private Button btnTwo;
    private Button model;
    private Camera camera = null;
    String str = android.os.Build.MODEL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        btnOne = (Button)findViewById(R.id.btn_one);
        btnTwo = (Button) findViewById(R.id.btn_two);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        info = getData();
        Entry entry = new Entry(getBaseContext(),1,8,10);


        model = (Button) findViewById(R.id.model);
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setText(str);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Entry.exitLogCatcher();
    }

    public ArrayList<BasicInfo> getData() {
        ArrayList<BasicInfo> data = new ArrayList<BasicInfo>();
        BasicInfo basic1 = new BasicInfo();
        basic1.setSn("111");
        basic1.setModel("222");
        LoggerUtils.d(basic1.toString());

        label1://测试label跳转
        for (int i = 0; i < 10; i++) {
            System.out.println("i = " + i);
            for (int x = 0; x < 10; x++) {
                System.out.println("x = " + x);
                break label1;
            }
        }
        return data;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one:
                BasicInfo basic1 = new BasicInfo();
                basic1.setSn("111");
                basic1.setModel("222");
                btnOne.setText(basic1.toString());
                if (camera != null){
                    LoggerUtils.d("luo---camera != null");
                    camera.release();
                }
                break;
            case R.id.btn_two:
                List<LogConfigInfo> lists = Entry.queryAll();
                if (lists != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < lists.size(); i++) {
                        LogConfigInfo logConfigInfo1 = lists.get(i);
                        sb.append(logConfigInfo1.toString() + "\n");
                        Log.i(TAG, "onClick: ---------allInfo: " + logConfigInfo1.toString());
                    }
                    btnTwo.setText(sb.toString());
                }else {
                    Log.i(TAG, "onClick: ----------未找到包名对应记录");
                    Toast.makeText(getBaseContext(), "未找到包名对应记录", Toast.LENGTH_LONG);
                }
                break;
        }
    }


}
