package com.example.wuxio.gankexamples.gank.beauty;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author wuxio 2018-05-05:11:16
 */
public class ImageLoadRunnable implements Runnable {

    private static final String TAG = "ImageLoadRunnable";
    private List< String > urls;
    private List< File >   mBitmapFiles;


    public ImageLoadRunnable(List< String > urls) {

        this.urls = urls;
    }


    @Override
    public void run() {

        if (urls == null || urls.size() == 0) {
            return;
        }

        File picFile = FileManager.getAppPicFile();
        int size = urls.size();
        mBitmapFiles = new ArrayList<>(size);
        List< Callable< File > > callableList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String url = urls.get(i);
            String name = FileNameUtils.makeName(url);

            File pic = new File(picFile, name);
            if (pic.exists()) {
                mBitmapFiles.add(pic);
            } else {

            }
        }
    }
}
