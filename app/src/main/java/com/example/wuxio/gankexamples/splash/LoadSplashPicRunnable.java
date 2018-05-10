package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.ImageView;

import com.example.wuxio.gankexamples.constant.ImageUrl;
import com.example.wuxio.gankexamples.gank.beauty.ImageCallable;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-08:7:16
 */
public class LoadSplashPicRunnable implements Runnable {

    private final int                             mWidth;
    private final int                             mHeight;
    private       Bitmap                          mBitmap;
    private       WeakReference< SplashActivity > mReference;


    public LoadSplashPicRunnable(SplashActivity splashActivity) {

        mReference = new WeakReference<>(splashActivity);

        ImageView image = splashActivity.getLogoImage();
        mWidth = image.getWidth();
        mHeight = image.getHeight();
    }


    @Override
    public void run() {

        String[] urls = ImageUrl.TRANSITION_URLS;

        ImageCallable callable = new ImageCallable(urls[1]);

        try {
            Pair< String, File > result = callable.call();

            File file = result.second;

            mBitmap = BitmapReader.decodeSampledBitmap(file, mWidth, mHeight);

            mReference.get().setSplash(mBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
