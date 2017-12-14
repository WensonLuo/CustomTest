package com.newpos.upos.customtext.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * *
 * 
 * @author JornyChen
 * @version 1.1 2014-4-28
 * 
 */
public class ImageUtil {

	private static final String TAG = "ImageUtil";

	public static Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * * 把View变成图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	// byte[]转换成Bitmap
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**
	 * Draw the view into a bitmap.
	 */
	public static Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);
		v.setDrawingCacheEnabled(true);
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		float alpha = v.getAlpha();
		v.setAlpha(1.0f);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();

		if (cacheBitmap == null) {

			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		v.destroyDrawingCache();
		v.setAlpha(alpha);
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/***
	 * ViewGroup 2bitmap *
	 * 
	 * @param w
	 * @param h
	 * @param v
	 * @return
	 */
	public static Bitmap getviewgroup2Bitmap(int w, int h, View v) {

		Bitmap b = Bitmap.createBitmap(w, h, Config.RGB_565);
		Canvas cv = new Canvas(b);
		v.draw(cv);
		return b;

	}

	/**
	 * 计算文字长度并截取
	 * 
	 * @param title
	 *            内容
	 * @param text_size
	 *            文字大小
	 * @param linkwidth
	 *            限制的宽度
	 * @return
	 */
	public static String measureTitle(String title, int text_size, int linkwidth) {
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(text_size);

		if (textPaint.measureText(title, 0, title.length()) < linkwidth)
			return title;

		int i;
		String str = null;
		for (i = title.length(); i > 0; i--) {
			str = title.substring(0, i) + "...";
			float width = textPaint.measureText(str);
			if (width < linkwidth) {
				break;
			}
		}

		return str;
	}

	// public static Bitmap getDrawSingleBigOrSmallCell(Activity mContext, float
	// w, float h, Bitmap iconBitmap,
	// String title, Bitmap bgColor, int cellSizeType, int cellTextSize, int
	// textPadding, float iconDistanceX,
	// float iconDistanceY) {
	//
	// Bitmap b = Bitmap.createScaledBitmap(bgColor, (int) w, (int) h, true);
	//
	// Canvas canvas = new Canvas(b);
	// canvas.save();
	// Paint mPaint = new Paint();
	// mPaint.setAntiAlias(true);// 去除锯齿
	// mPaint.setFilterBitmap(true);// 对位图进行滤波处理
	// canvas.drawBitmap(iconBitmap, iconDistanceX, iconDistanceY, mPaint);
	// if (cellSizeType != Cell.CELL_SIZE_SMALL) {
	// title = measureTitle(title, cellTextSize, (int) (w - textPadding * 2));
	// Paint mPaintText = new Paint();
	// mPaintText.setColor(Color.WHITE);
	// mPaintText.setAntiAlias(true);// 去除锯齿
	// mPaintText.setFilterBitmap(true);// 对位图进行滤波处理
	// mPaintText.setTextSize(cellTextSize);
	// mPaintText.setTypeface(CellUtil.getTypeface(mContext));// 可用
	// canvas.drawText(title, textPadding + 5, (int) h - (textPadding * 2) - 2,
	// mPaintText);
	// }
	// canvas.restore();
	//
	// return b;
	//
	// }

	// GameView.drawImage(canvas, mBitDestTop, miDTX, mBitQQ.getHeight(),
	// mBitDestTop.getWidth(), mBitDestTop.getHeight()/2, 0, 0);
	public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
			int w, int h, int bx, int by) { // x,y表示绘画的起点，
		Rect src = new Rect();// 图片
		Rect dst = new Rect();// 屏幕位置及尺寸
		// src 这个是表示绘画图片的大小
		src.left = bx; // 0,0
		src.top = by;
		src.right = bx + w;// mBitDestTop.getWidth();,这个是桌面图的宽度，
		src.bottom = by + h;// mBitDestTop.getHeight()/2;// 这个是桌面图的高度的一半
		// 下面的 dst 是表示 绘画这个图片的位置
		dst.left = x; // miDTX,//这个是可以改变的，也就是绘图的起点X位置
		dst.top = y; // mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
		dst.right = x + w; // miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角
		dst.bottom = y + h; // mBitQQ.getHeight() +
							// mBitDestTop.getHeight();//表示需绘画的图片的右下角
		canvas.drawBitmap(blt, src, dst, null);// 这个方法 第一个参数是图片原来的大小，第二个参数是
												// 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置

		src = null;
		dst = null;
	}

	public static Drawable convertBitmap2Drawable(Context context, Bitmap bitmap) {

		BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);

		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}

	public static Drawable Bytes2Drawable(Context context, byte[] b) {
		if (b == null) {
			return null;
		}
		if (b.length != 0) {

			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

			return convertBitmap2Drawable(context, bitmap);
		} else {
			return null;
		}

	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// Drawable转换成byte[]
	public static byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2Bytes(bitmap);
	}

	/**
	 * *
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * *
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable, int width,
			int height) {

		// Bitmap bitmap = Bitmap.createBitmap(width, height,
		// Bitmap.Config.ARGB_8888);
		// Canvas canvas = new Canvas(bitmap);
		// //drawable.setBounds(0, 0, width, height);
		// drawable.draw(canvas);

		Bitmap b = Bitmap.createScaledBitmap(drawable2Bitmap(drawable), width,
				height, true);
		return b;
	}

	/**
	 * 
	 * {放大图片}
	 * 
	 * @param b
	 *            图
	 * @param x
	 *            尺寸x
	 * @param y
	 *            尺寸y
	 * @return
	 */
	public static Bitmap scaleBig(Bitmap b, float x, float y) {
		int w = b.getWidth();
		int h = b.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(x, y); // 长和宽放大缩小的比例
		System.out.println("width:" + b.getWidth() + " h: " + b.getHeight()
				+ "y:" + y + "x" + x);
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

	public static Drawable getscaleIcon(Context context, Bitmap b, float x,
			float y) {
		return convertBitmap2Drawable(context, scaleBig(b, x, y));

	}

	/**
	 * 
	 * {放大图片}
	 * 
	 * @param b
	 *            图
	 * @param x
	 *            尺寸x
	 * @param y
	 *            尺寸y
	 * @return
	 */
	public static Bitmap scaleBig(Bitmap b, float x, float y, float centerX,
			float centerY) {
		int w = b.getWidth();
		int h = b.getHeight();
		Matrix matrix = new Matrix();
		// matrix.postScale(x , y); // 长和宽放大缩小的比例
		matrix.setScale(x, y, centerX, centerY);
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

	public static int Px2Dip(final Context context, int px) {
		return (int) (px / context.getResources().getDisplayMetrics().density);
	}

	public static int Dip2Px(final Context context, int dip) {
		return (int) (dip * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * 将Bitmap保存到SD卡中
	 * 
	 * @param fileName
	 * @param mBitmap
	 */
	public static void saveBitmapToSDCard(String fileName, Bitmap mBitmap) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName + ".png");
			try {
				f.createNewFile();
			} catch (IOException e) {
				Log.i(TAG, "create file error!");
			}
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap createReflectedImage(View view) {
		return createReflectedImage(convertViewToBitmap(view));
	}

	public static Bitmap createReflectedImage(Bitmap originalImage) {

		final int reflectionGap = 4;
		int width = originalImage.getWidth(); // 图片原始高度
		int height = originalImage.getHeight();// 图片原始宽度
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1); // 放大
		// Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
		// 0, width, height / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height / 2),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);
		Paint defaultPaint = new Paint();

		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		canvas.drawBitmap(originalImage, 0, reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, 0, 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * 
	 * @param sourceImg
	 *            传入的图片
	 * @param number
	 *            0-100(0为完全透明，100为不透明)
	 * @return Bitmap 处理后的图片
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());
		number = number * 255 / 100;
		double round = (double) number / (double) (argb.length);
		System.out.println(round + "  l=" + argb.length + " n=" + number);
		for (int i = 0; i < argb.length; i++) {
			if (number - i * round > 10) {
				argb[i] = ((int) (number - i * round) << 24)
						| (argb[i] & 0x00FFFFFF);
				continue;
			} else {
				argb[i] = (10 << 24) | (argb[i] & 0x00FFFFFF);
				continue;
			}

		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;
	}

	/**
	 * 传入一张图片，经倒转后，再取一半
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap setShadow(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap shadowImage = Bitmap.createBitmap(bitmap, 0, height / 2, width,
				height / 2, matrix, false);
		return shadowImage;
	}

	public static Bitmap getImageFromPath(Context mContext, String fileName,
			String dirPath) {

		return getImageFromPath(mContext, dirPath + "/" + fileName);
	}

	public static Bitmap getImageFromPath(Context mContext, String path) {
		Bitmap image = null;
		File file = new File(path);
		try {
			InputStream is = new FileInputStream(file);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static Bitmap getImageFromAssetsFile(Context mContext,
			String fileName) {
		Bitmap image = null;
		AssetManager am = mContext.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static int getRadonColorNumber() {
		Random mRandom = new Random();
		return mRandom.nextInt(3) + 1;
	}

	// 根据需要获取bitmap。
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@SuppressLint("NewApi")
	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

		if (VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

			final RenderScript rs = RenderScript.create(context);
			final Allocation input = Allocation.createFromBitmap(rs,
					sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final Allocation output = Allocation.createTyped(rs,
					input.getType());
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
					Element.U8_4(rs));
			script.setRadius(radius /* e.g. 3.f */);
			script.setInput(input);
			script.forEach(output);
			output.copyTo(bitmap);
			return bitmap;
		}

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

	/**
	 * 获得圆角图片的方法
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}
