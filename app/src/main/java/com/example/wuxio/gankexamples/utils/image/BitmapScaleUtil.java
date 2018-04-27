package com.example.wuxio.gankexamples.utils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.InputStream;

/**
 * @author wuxio
 * @date 2017-05-30
 */
public class BitmapScaleUtil {

    /**
     * 解析图片，生成Bitmap对象.从资源文件中解析
     */
    public static Bitmap decodeSampledBitmap(Resources res, int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * 解析图片，生成Bitmap对象.从资源文件中解析
     */
    public static Bitmap decodeMaxSampledBitmap(Resources res, int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateMaxInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * 解析图片，生成Bitmap对象。使用本地文件
     */
    public static Bitmap decodeSampledBitmap(File file, int reqWidth, int reqHeight) {

        String path = file.getPath();
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path);
    }


    /**
     * 解析图片，生成Bitmap对象。使用本地文件
     */
    public static Bitmap decodeMaxSampledBitmap(File file, int reqWidth, int reqHeight) {

        String path = file.getPath();
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateMaxInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path);
    }


    /**
     * 解析图片，生成Bitmap对象。流文件
     */
    public static Bitmap decodeSampledBitmap(InputStream input, int reqWidth, int reqHeight) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(input, null, options);
    }


    /**
     * 解析图片，生成Bitmap对象。流文件
     */
    public static Bitmap decodeMaxSampledBitmap(InputStream input, int reqWidth, int reqHeight) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateMaxInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(input, null, options);
    }


    /**
     * 计算图片缩放比例
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //如果图片宽或者高 大于 view的宽高
        if (height > reqHeight || width > reqWidth) {

            //计算图片的宽高的一半
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //如果原始图片宽高是 view宽高的 2,4,8,16 ... 倍以上,会压缩图片至宽高任意一条边略大于 view 的 宽高为止,图片质量不会太差
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    /**
     * 计算图片极限缩放比例,使用宽高采样率中最大的
     */
    private static int calculateMaxInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //如果图片宽或者高 大于 view的宽高
        if (height > reqHeight || width > reqWidth) {

            //计算图片的宽高的一半
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //分别计算宽高采样率,使用其中最大的值
            while ((halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }

            int heightSampleSize = inSampleSize;

            while ((halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }

            inSampleSize = heightSampleSize > inSampleSize ? heightSampleSize : inSampleSize;
        }
        return inSampleSize;
    }
}