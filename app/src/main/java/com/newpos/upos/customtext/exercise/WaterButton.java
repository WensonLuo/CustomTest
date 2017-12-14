package com.newpos.upos.customtext.exercise;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.newpos.upos.customtext.R;

/**
 * Created by Wenson_Luo on 2017/9/6.
 */

public class WaterButton extends AppCompatImageButton {

    public static final int INVALIDATE_DURATION = 15;
    private static int DIFFUSE_GAP = 10;
    private static int TAP_TIMEOUT;

    private int viewWidth, viewHeight;
    private int pointX, pointY;
    private int maxRadius;
    private int shaderRadius;

    private Paint bottomPaint, colorPaint;
    private boolean isPushButton;

    private int eventX, eventY;
    private long downTime = 0;//按下的时间

    public WaterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        //短按走向长按前的毫秒时间
        TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        Log.d("luo---", "TAP_TIMEOUT: " + TAP_TIMEOUT);
    }

    private void initPaint() {
        colorPaint = new Paint();
        bottomPaint = new Paint();
        colorPaint.setColor(getResources().getColor(R.color.reveal_color));
        bottomPaint.setColor(getResources().getColor(R.color.bottom_color));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);//如果第一次view第一次添加到布局，w h 是当前宽高，old是0
        this.viewWidth = w;
        this.viewHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (downTime == 0) {
                    downTime = SystemClock.elapsedRealtime();//拿到按下的时间
                }
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                //计算最大半径：
                countMaxRadius();
                isPushButton = true;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                if (SystemClock.elapsedRealtime() - downTime < TAP_TIMEOUT) {
                    DIFFUSE_GAP = 30;//短按则扩散更快
                } else {
                    clearData();//长按恢复数据
                }
        }
        return super.onTouchEvent(event);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isPushButton) {
            return;
        }
        canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
        canvas.save();
        canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
        canvas.drawCircle(eventX, eventY, shaderRadius, colorPaint);
        canvas.restore();

        //阴影半径不断增加，直到最大
        if (shaderRadius < maxRadius) {
            postInvalidateDelayed(INVALIDATE_DURATION, pointX, pointY, pointX + viewWidth, pointY + viewHeight);
            shaderRadius += DIFFUSE_GAP;
        } else {
            clearData();
        }
    }

    private void clearData() {
        downTime = 0;
        DIFFUSE_GAP = 10;
        isPushButton = false;
        shaderRadius = 0;
        postInvalidate();//刷新UI
    }

    private void countMaxRadius() {
        if (viewWidth > viewHeight) {
            if (eventX < viewWidth / 2) {
                maxRadius = viewWidth - eventX;
            } else {
                maxRadius = eventX;
            }
        } else {
            if (eventY < viewHeight / 2) {
                maxRadius = viewHeight - eventY;
            } else {
                maxRadius = eventY;
            }
        }
    }
}
