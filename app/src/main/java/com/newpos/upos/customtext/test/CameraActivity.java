package com.newpos.upos.customtext.test;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.newpos.loghelper.Entry;
import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.base.BaseActivity;
import com.newpos.upos.customtext.exercise.parcel.BasicInfo;
import com.newpos.upos.customtext.utils.LoggerUtils;

import java.util.ArrayList;

/**
 * Created by Wenson_Luo on 2017/7/22.
 */

public class CameraActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OneActivity";
    private Button btnOne;
    private Button btnTwo;
    private Camera camera = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        btnOne = (Button)findViewById(R.id.btn_one);
        btnTwo = (Button) findViewById(R.id.btn_two);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
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

        Camera.open(0);

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
                if (camera != null){
                    LoggerUtils.d("luo---camera != null");
                    camera.release();
                }

                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                LoggerUtils.d("luo--- 摄像头："+cameraInfo.facing);//打印摄像头前置或者后置
                if (camera == null){
                    LoggerUtils.d("luo---camera open fail");
                }else {
                    camera = Camera.open(0);
                    Intent intent = new Intent(); //调用照相机
                    intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
                    startActivity(intent);
                    LoggerUtils.d("luo---camera open succeed");
                }
                break;
            case R.id.btn_two:
                if (camera != null){
                    LoggerUtils.d("luo---camera != null");
                    camera.release();
                }
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                Camera.CameraInfo cameraInfo1 = new Camera.CameraInfo();
                LoggerUtils.d("luo--- 摄像头："+cameraInfo1.facing);//打印摄像头前置或者后置
                if (camera == null){
                    LoggerUtils.d("luo---camera open fail");
                }else {
//                    Intent intent = new Intent(); //调用照相机
//                    intent.putExtra("camerasensortype", 2); // 调用前置摄像头
//                    intent.putExtra("autofocus", true); // 自动对焦
//                    intent.putExtra("fullScreen", false); // 全屏
//                    intent.putExtra("showActionIcons", false);
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivity(intent);
                    LoggerUtils.d("luo---camera open succeed");
                }
                break;
        }
    }


}
