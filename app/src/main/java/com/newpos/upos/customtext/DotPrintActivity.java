package com.newpos.upos.customtext;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newpos.loghelper.Entry;
import com.newpos.upos.customtext.base.BaseActivity;
import com.newpos.upos.customtext.utils.LoggerUtils;
import com.newpos.upos.customtext.view.CustomText;

/**
 * 测试点阵打印
 */
public class DotPrintActivity extends BaseActivity {
    private static final String TAG = "DotPrintActivity";
    public String str;//输入的字符串
    public float radius;//点半径
    public int dotCount;//点阵数

    TextView tvFont;
    EditText tvFont1;
    EditText tvFont2;
    EditText tvFont3;
    Button button;
    CustomText showText;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFont = (TextView) findViewById(R.id.tv_font);
        tvFont1 = (EditText) findViewById(R.id.tv_font1);
        tvFont2 = (EditText) findViewById(R.id.tv_font2);
        tvFont3 = (EditText) findViewById(R.id.tv_font3);
        button = (Button) findViewById(R.id.btn);
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "DotMatrix.ttf");
        tvFont.setTypeface(typeface);
        tvFont.setTextSize(30);
        tvFont.setText("中文无效，Hey,Girl!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showText != null) {
                    LoggerUtils.d("luo---移除view");
                    linearLayout.removeView(showText);
                }
                str = tvFont1.getText().toString();
                radius = Integer.parseInt(tvFont2.getText().toString());
                int temp = Integer.parseInt(tvFont3.getText().toString());
                dotCount = temp + 8;
                LoggerUtils.d("luo---打印字符串：" + str + "；点半径：" + radius + "；点阵数：" + dotCount + " * " + dotCount);
                showText = new CustomText(getBaseContext(),str, radius, dotCount);
                linearLayout.addView(showText);
            }
        });

        Entry entry = new Entry(getBaseContext(),3,6,8);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ------------");
        Entry.exitLogCatcher();
    }
}
