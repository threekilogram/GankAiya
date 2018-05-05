package com.example.wuxio.gankexamples.gank.beauty;

import android.graphics.Bitmap;

import com.example.wuxio.gankexamples.file.FileNameUtils;

import java.util.List;

/**
 * @author wuxio 2018-05-05:11:16
 */
public class ImageLoadRunnable implements Runnable {

    private static final String TAG = "ImageLoadRunnable";
    private List< String > urls;
    private List< Bitmap > mBitmaps;


    public ImageLoadRunnable(List< String > urls) {

        this.urls = urls;
    }


    @Override
    public void run() {

        if (urls == null || urls.size() == 0) {
            return;
        }

        int size = urls.size();
        for (int i = 0; i < size; i++) {
            String url = urls.get(i);
            String name = FileNameUtils.makeName(url);

        }
    }
}
