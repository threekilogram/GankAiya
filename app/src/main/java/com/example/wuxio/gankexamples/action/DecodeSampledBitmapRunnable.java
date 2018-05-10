package com.example.wuxio.gankexamples.action;

import android.graphics.Bitmap;
import android.util.Pair;

import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.gank.beauty.ImageCallable;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;

/**
 * 该 runnable 用于加载一个Url对应的图片,并且缩放
 *
 * @author wuxio 2018-05-08:7:16
 */
public class DecodeSampledBitmapRunnable implements Runnable {

    private String mUrl;
    private int    mWidth;
    private int    mHeight;
    private Bitmap mBitmap;


    public Bitmap getBitmap() {

        return mBitmap;
    }


    public DecodeSampledBitmapRunnable(String url, int width, int height) {

        mWidth = width;
        mHeight = height;
        mUrl = url;
    }


    @Override
    public void run() {

        ImageCallable callable = new ImageCallable(mUrl);

        try {
            Pair< String, File > result = callable.call();

            File file = result.second;

            mBitmap = BitmapReader.decodeSampledBitmap(file, mWidth, mHeight);

        } catch (Exception e) {
            e.printStackTrace();

            /* 加载失败之后,加载默认失败图片 */

            mBitmap = BitmapReader.decodeSampledBitmap(
                    App.INSTANCE.getResources(),
                    R.drawable.without_net,
                    mWidth,
                    mHeight
            );
        }
    }
}
