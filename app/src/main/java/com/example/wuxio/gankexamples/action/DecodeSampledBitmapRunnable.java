package com.example.wuxio.gankexamples.action;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;

/**
 * 该 runnable 用于加载一个Url对应的图片,并且缩放
 *
 * @author wuxio 2018-05-08:7:16
 */
public class DecodeSampledBitmapRunnable implements Runnable {

    private File   mFile;
    private int    mWidth;
    private int    mHeight;
    private int    mFailedRes;
    private Bitmap mBitmap;


    public Bitmap getBitmap() {

        return mBitmap;
    }


    public DecodeSampledBitmapRunnable(File file, int width, int height, @DrawableRes int failedRes) {

        mFile = file;
        mWidth = width;
        mHeight = height;
        mFailedRes = failedRes;
    }


    @Override
    public void run() {

        try {

            mBitmap = BitmapReader.decodeSampledBitmap(mFile, mWidth, mHeight);

        } catch (Exception e) {
            e.printStackTrace();

            /* 加载失败之后,加载默认失败图片 */

            mBitmap = BitmapReader.decodeSampledBitmap(
                    App.INSTANCE.getResources(),
                    mFailedRes,
                    mWidth,
                    mHeight
            );
        }
    }
}
