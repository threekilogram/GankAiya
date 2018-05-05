package com.example.wuxio.gankexamples.gank.beauty;

import android.util.Log;

import java.util.List;

/**
 * @author wuxio 2018-05-05:11:16
 */
public class ImageLoadRunnable implements Runnable {

    private static final String TAG = "ImageLoadRunnable";
    private List< String > urls;


    public ImageLoadRunnable(List< String > urls) {

        this.urls = urls;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void run() {

        int size = urls.size();
        for (int i = 0; i < size; i++) {
            String url = urls.get(i);
            Log.i(TAG, "run:" + url);
        }
    }
}
