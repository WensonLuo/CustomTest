package com.newpos.upos.customtext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.newpos.upos.customtext.utils.LoggerUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Polo on 2017/3/22.
 * 测试点阵打印字符串
 */
public class CustomText extends View {

    public static final int EN_TYPE = 0;
    public static final int CN_TYPE = 1;
    /**
     * 点阵view
     *
     * @param context
     */
    private final static String ENCODE = "GB2312";//编码格式
    //    private final static String ENCODE = "UTF-8";
    private final static String ZK16 = "HZK16";//assets下的路径
    private final static String ZK24 = "HZK24";
    private final static String ZK32 = "HZK32";
    private final static String ASC16 = "ASC16";//assets下的路径
    private Paint textPaint;   //绘制黑点
    private Paint smallPaint;   //绘制红点
    private Canvas mCanvas;
    private Context mContext;
    private byte[][] arr;//返回的二位数组
//    private int all_16_32 = 16;//16*16
//    private int all_2_4 = 2;//一个汉字等于两个字节
//    private int all_32_128 = 32;//汉字解析成16*16 所占字节数
//    private int font_width = 8;//ascii码 8*16
//    private int font_height = 16;//ascii码 8*16
//    private int all_16 = 16;//ascii码解析成8*16 所占字节数
    private String str = "";//输入的字符串
    private float radius = 0;//点半径
    private int dotCount = 0;//点阵数

    public CustomText(Context context, String str, float radius, int dotCount) {
        super(context);
        this.mContext = context;
        this.str = str;
        this.radius = radius;
        this.dotCount = dotCount;
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        smallPaint = new Paint();
        smallPaint.setColor(Color.WHITE);
        LoggerUtils.d("luo---mCanvas:"+mCanvas);
        if (mCanvas != null) {
            mCanvas.restore();
        }else {
            mCanvas = new Canvas();
        }
        LoggerUtils.d("luo---draw()");
        draw(mCanvas);
    }


    public CustomText(Context context) {
        super(context);
    }

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LoggerUtils.d("luo---onDraw()，canvas："+canvas);
        canvas.drawColor(Color.WHITE);
        resolveString(str, canvas);
        LoggerUtils.d("luo---看看到不到这里");
    }

    /**
     * 解析成点阵
     *
     * @param str
     * @return
     */
    private byte[][] resolveString(String str, Canvas canvas) {
        byte[] data = null;
        int[] code = null;
        int byteCount;//字节数
        int lCount;//位数
        int COUNT_EN = 0;
        int COUNT_CN = 0;
        for (int i = 0; i < str.length(); i++) {
            LoggerUtils.d("第====" + i + "====次字符进入");
            if (str.charAt(i) < 0x80) {
                // 字母 数字
                COUNT_EN++;
                LoggerUtils.w("字符=====" + str.charAt(i) + "=====进入" + "字符出现次数:" + COUNT_EN);
                arr = new byte[dotCount][dotCount/2];
                data = read_a(str.charAt(i));
                byteCount = 0;//字节数
                for (int line = 0; line < 16; line++) {
                    lCount = 0;//列数
                    for (int k = 0; k < 1; k++) {
                        for (int j = 0; j < 8; j++) {
                            if (((data[byteCount] >> (7 - j)) & 0x1) == 1) {
                                arr[line][lCount] = 1;
                                canvas.drawCircle(getXPosition(lCount, i, COUNT_EN, COUNT_CN, EN_TYPE), getYPosition(line), radius, textPaint);
//                                LoggerUtils.w("绘制：lCount:" + lCount + "===line:" + line);
                            } else {
                                arr[line][lCount] = 0;
//                                canvas.drawCircle(getXPosition(lCount, i, COUNT_EN, COUNT_CN, EN_TYPE), getYPosition(line), radius, smallPaint);
//                                LoggerUtils.w("luo---绘制红点：lCount:" + lCount + "===line:" + line);
                            }
                            lCount++;
                        }
                        byteCount++;
                    }
                    System.out.println();
                }
            } else {
                COUNT_CN++;
                LoggerUtils.w("汉字=======" + str.charAt(i) + "========进入" + "字符出现次数:" + COUNT_CN);
                arr = new byte[dotCount][dotCount];//16行，16列
                code = getByteCode(str.substring(i, i + 1));
                data = read(code[0], code[1]);
                byteCount = 0;
                for (int line = 0; line < dotCount; line++) {
                    lCount = 0;
                    for (int k = 0; k < 2; k++) {
                        for (int j = 0; j < 8; j++) {
                            if (((data[byteCount] >> (7 - j)) & 0x1) == 1) {
                                arr[line][lCount] = 1;
                                canvas.drawCircle(getXPosition(lCount, i, COUNT_EN, COUNT_CN, CN_TYPE), getYPosition(line), radius, textPaint);
//                                LoggerUtils.w("绘制：lCount:" + lCount + "===line:" + line);
                            } else {
                                arr[line][lCount] = 0;
//                                canvas.drawCircle(getXPosition(lCount, i, COUNT_EN, COUNT_CN, CN_TYPE), getYPosition(line), radius, smallPaint);
//                                LoggerUtils.w("luo---绘制红点：lCount:" + lCount + "===line:" + line);
                            }
                            lCount++;
                        }
                        byteCount++;
                    }
                    System.out.println();
                }
            }
        }
        return arr;
    }

    /**
     * 获取汉字的区，位（ascii码不需要区码，位码）
     *
     * @param str
     * @return
     */
    private int[] getByteCode(String str) {
        int[] byteCode = new int[2];
        try {
            byte[] data = str.getBytes(ENCODE);
            byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
            byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return byteCode;
    }

    /**
     * 读取字库中的ASCII 码
     */
    private byte[] read_a(char char_num) {
        LoggerUtils.d("luo---read_a()");
        byte[] data = null;
        int ascii = (int) char_num;
        try {
            data = new byte[dotCount];//定义缓存区的大小
            InputStream inputStream = mContext.getResources().getAssets()
                    .open(ASC16);//打开ascii字库的流
            int offset = ascii * 16;//ascii码在字库里的偏移量
            inputStream.skip(offset);
            inputStream.read(data, 0, dotCount);//读取字库中ascii码点阵数据
            inputStream.close();
            return data;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 读取字库中的汉字
     *
     * @param areaCode
     * @param posCode
     * @return
     */
    private byte[] read(int areaCode, int posCode) {
        byte[] data = null;
        try {
            int area = areaCode - 0xa0;//区码
            int pos = posCode - 0xa0;//位码

            InputStream in = null;//打开中文字库的流
            switch (dotCount){
                case 16:
                    in = mContext.getResources().getAssets().open(ZK16);
                    break;
                case 24:
                    in = mContext.getResources().getAssets().open(ZK24);
                    break;
                case 32:
                    in = mContext.getResources().getAssets().open(ZK32);
                    break;
            }
            long offset = dotCount * 2 * ((area - 1) * 94 + pos - 1);//汉字在字库里的偏移量
            in.skip(offset);//跳过偏移量
            data = new byte[dotCount * 2];//定义缓存区的大小
            in.read(data, 0, dotCount * 2);//读取该汉字的点阵数据
            in.close();
        } catch (Exception ex) {
        }
        return data;
    }

    /**
     * 获取绘制第column列的点的X坐标，计算每个字的点偏移量
     *
     * @param column   当前点的列数
     * @param i
     * @param COUNT_EN 当前字符总数中 英文或数字的个数总和
     * @param COUNT_CN 当前字符总数中 汉字个数总和
     * @param type     当前字符是汉字或英文数字类型
     * @return
     */
    private float getXPosition(int column, int i, int COUNT_EN, int COUNT_CN, int type) {
        switch (type) {
            case EN_TYPE:
                return column * radius * 2 + ((COUNT_CN) * radius * 2 * 16) + (COUNT_EN - 1) * radius * 2 * 8;
            case CN_TYPE:
                return column * radius * 2 + ((COUNT_CN - 1) * radius * 2 * 16) + (COUNT_EN) * radius * 2 * 8;
        }
        return column * radius * 2 + i * radius * 2 * 16;
    }

    private float getTotalXPosition(int COUNT_EN, int COUNT_CN) {
        return COUNT_EN * 8 + COUNT_CN * 16;
    }

    /**
     * 获取绘制第row行的点的Y坐标
     *
     * @param row
     * @return
     */
    private float getYPosition(int row) {
        return radius + radius * 2 * row;
    }
}
