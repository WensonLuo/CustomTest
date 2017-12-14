package com.newpos.upos.customtext.exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.newpos.upos.customtext.R;

/**
 * Created by Wenson_Luo on 2017/9/6.
 */

public class WaterBtnActivity extends AppCompatActivity {
    private WaterButton waterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_btn);
        waterButton = (WaterButton) findViewById(R.id.water_btn);
    }
}
