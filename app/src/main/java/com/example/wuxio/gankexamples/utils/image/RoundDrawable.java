package com.example.wuxio.gankexamples.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * @author wuxio
 */
public class RoundDrawable extends Drawable {

    private Paint  paint;
    private RectF  rectF;
    private int    radius;
    private Bitmap bitmap;


    public RoundDrawable(Context context, int resID) {

        this(BitmapFactory.decodeResource(context.getResources(), resID));
    }


    public RoundDrawable(Bitmap bitmap) {

        this.bitmap = bitmap;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        //为画笔设置位图渲染
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        //初始化半径
        radius = width / 2;
    }


    /**
     * drawable将被绘制在画布上的区域
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {

        super.setBounds(left, top, right, bottom);
        //绘制区域
        rectF = new RectF(left, top, right, bottom);
    }


    /**
     * 核心方法
     */
    @Override
    public void draw(@NonNull Canvas canvas) {

        canvas.drawRoundRect(rectF, radius, radius, paint);
    }


    @Override
    public void setAlpha(int i) {

        paint.setAlpha(i);
        invalidateSelf();
    }


    @Override
    public int getIntrinsicHeight() {

        return bitmap.getHeight();
    }


    @Override
    public int getIntrinsicWidth() {

        return bitmap.getWidth();
    }


    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }


    @Override
    public int getOpacity() {

        return PixelFormat.TRANSLUCENT;
    }
}