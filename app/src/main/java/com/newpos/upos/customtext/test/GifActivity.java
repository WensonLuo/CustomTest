package com.newpos.upos.customtext.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.newpos.upos.customtext.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Wenson_Luo on 2017/7/11.
 * 测试POS机显示 Gif 图片，没有出现渲染异常
 */

public class GifActivity extends AppCompatActivity {
    private GifImageView gifImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        gifImageView = (GifImageView) findViewById(R.id.gif);
        gifImageView.setImageResource(R.drawable.gif_process);
    }
}
