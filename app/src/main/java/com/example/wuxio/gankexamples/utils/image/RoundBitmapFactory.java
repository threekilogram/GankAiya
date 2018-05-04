package com.example.wuxio.gankexamples.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * @author wuxio
 * @date 2017-12-25
 */
public class RoundBitmapFactory {

    /**
     * 利用BitmapShader绘制圆角图片,和原始bitmap尺寸相同
     *
     * @param bitmap 待处理图片
     * @param radius 圆角半径大小
     * @return 结果图片
     */
    public static Bitmap roundBitmapByShader(Bitmap bitmap, int radius) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 初始化绘制纹理图(使用bitmap 填充纹理)
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔将纹理图绘制到画布上面
        // 缺点:如果图片宽高不一致,渲染之后是圆角矩形,解决方法,从原图截取中间宽高相等的一部分用来渲染
        targetCanvas.drawRoundRect(new RectF(0, 0, bitmapWidth, bitmapHeight), radius, radius, paint);

        return targetBitmap;
    }


    /**
     * 利用BitmapShader绘制圆角图片,如果原始图片宽高不一致,截取中间正方形部分
     *
     * @param bitmap 待处理图片
     * @return 结果图片
     */
    public static Bitmap circleBitmapByShader(Bitmap bitmap) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        bitmap = createSquareBitmap(bitmap);

        int radius = bitmap.getWidth() / 2;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 初始化绘制纹理图(使用bitmap 填充纹理)
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔将纹理图绘制到画布上面
        // 缺点:如果图片宽高不一致,渲染之后是圆角矩形,解决方法,从原图截取中间宽高相等的一部分用来渲染
        targetCanvas.drawRoundRect(new RectF(0, 0, bitmapWidth, bitmapHeight), radius, radius, paint);

        return targetBitmap;
    }


    /**
     * 利用Xfermode绘制圆角图片
     *
     * @param bitmap 待处理图片
     * @param radius 圆角半径大小
     * @return 结果图片
     */
    public static Bitmap roundBitmapByXfermode(Bitmap bitmap, int radius) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建原图bitmap
        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config
                .ARGB_8888);

        //装载画布,并将画布置为透明
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawARGB(0, 0, 0, 0);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, (float) bitmapWidth, (float) bitmapHeight);

        // 在画布上绘制圆角图
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 在画布上绘制原图片
        Rect ret = new Rect(0, 0, bitmapWidth, bitmapHeight);
        canvas.drawBitmap(newBt, ret, ret, paint);

        return targetBitmap;
    }


    /**
     * 利用Xfermode绘制圆角图片,如果原始图片宽高不一致,截取中间正方形部分
     *
     * @param bitmap 待处理图片
     * @return 结果图片
     */
    public static Bitmap circleBitmapByXfermode(Bitmap bitmap) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        bitmap = createSquareBitmap(bitmap);

        int radius = bitmap.getWidth() / 2;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建原图bitmap
        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config
                .ARGB_8888);

        //装载画布,并将画布置为透明
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawARGB(0, 0, 0, 0);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, (float) bitmapWidth, (float) bitmapHeight);

        // 在画布上绘制圆角图
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 在画布上绘制原图片
        Rect ret = new Rect(0, 0, bitmapWidth, bitmapHeight);
        canvas.drawBitmap(newBt, ret, ret, paint);

        return targetBitmap;
    }


    /**
     * 利用RoundedBitmapDrawable绘制圆角图片
     *
     * @param bitmap 待处理图片
     * @param radius 圆角半径大小
     * @return 结果图片
     */
    public static Drawable roundBitmap(Context context, Bitmap bitmap, int radius) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // 绘制圆角
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), newBt);
        dr.setCornerRadius(radius);
        dr.setAntiAlias(true);

        return dr;
    }


    /**
     * 利用RoundedBitmapDrawable绘制圆形图片
     *
     * @param bitmap 待处理图片
     * @return 结果图片
     */
    public static Drawable circleBitmap(Context context, Bitmap bitmap) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        bitmap = createSquareBitmap(bitmap);

        int radius = bitmap.getWidth() / 2;

        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // 绘制圆角
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), newBt);
        dr.setCornerRadius(radius);
        dr.setAntiAlias(true);

        return dr;
    }


    /**
     * 从bitmap中截取中间的正方形部分
     *
     * @param bitmap 原图
     * @return 原图的中间正方形部分
     */
    public static Bitmap createSquareBitmap(Bitmap bitmap) {

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 将bitmap 宽高中较小的值作为bitmap尺寸
        int finalSize = bitmapWidth >= bitmapHeight ? bitmapHeight : bitmapWidth;

        int xOffset = (bitmapWidth - finalSize) / 2;
        int yOffset = (bitmapHeight - finalSize) / 2;

        return Bitmap.createBitmap(bitmap, xOffset, yOffset, finalSize, finalSize);
    }
}
