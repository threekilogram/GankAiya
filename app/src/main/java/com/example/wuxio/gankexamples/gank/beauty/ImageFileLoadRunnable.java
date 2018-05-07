package com.example.wuxio.gankexamples.gank.beauty;

import android.graphics.Bitmap;
import android.util.ArrayMap;

import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于将本地 file 加载成 bitmap
 *
 * @author wuxio 2018-05-05:11:16
 */
public class ImageFileLoadRunnable implements Runnable {

    private static final String TAG = "ImageLoadRunnable";
    private List< String >           mUrls;
    private ArrayMap< String, File > mBitmapFiles;
    private List< Bitmap >           mBitmaps;
    private int                      mWidth;
    private int                      mHeight;


    public ImageFileLoadRunnable(
            List< String > urls,
            ArrayMap< String, File > bitmapFiles,
            int width,
            int height) {

        this.mUrls = urls;
        this.mBitmapFiles = bitmapFiles;
        mWidth = width;
        mHeight = height;
    }


    @Override
    public void run() {

        /* 按照顺序加载图片 */

        List< String > urls = mUrls;
        ArrayMap< String, File > bitmapFiles = mBitmapFiles;
        int width = mWidth;
        int height = mHeight;

        if (urls == null || urls.size() == 0) {
            return;
        }

        int size = urls.size();

        List< Bitmap > bitmaps = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String key = urls.get(i);
            File file = bitmapFiles.get(key);
            Bitmap bitmap = BitmapReader.decodeSampledBitmap(file, width, height);
            bitmaps.add(bitmap);
        }

        mBitmaps = bitmaps;
    }


    public List< Bitmap > getBitmaps() {

        return mBitmaps;
    }
}
