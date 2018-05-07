package com.example.wuxio.gankexamples.gank.beauty;

import android.util.ArrayMap;
import android.util.Pair;

import com.example.objectbus.executor.AppExecutor;
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
    private List< String >           urls;
    private ArrayMap< String, File > mBitmapFiles;


    public ImageLoadRunnable(List< String > urls) {

        this.urls = urls;
    }


    public ImageLoadRunnable() {

    }


    public void setUrls(List< String > urls) {

        this.urls = urls;
    }


    @Override
    public void run() {

        if (urls == null || urls.size() == 0) {
            return;
        }

        File picFile = FileManager.getAppPicFile();

        int size = urls.size();
        mBitmapFiles = new ArrayMap<>(size);
        List< Callable< Pair< String, File > > > callableList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String url = urls.get(i);
            String name = FileNameUtils.makeName(url);

            File pic = new File(picFile, name);
            if (pic.exists()) {

                mBitmapFiles.put(url, pic);
            } else {

                callableList.add(new ImageCallable(url));
            }
        }

        if (callableList.size() > 0) {
            List< Pair< String, File > > pairs = AppExecutor.submitAndGet(callableList);

            size = pairs.size();
            for (int i = 0; i < size; i++) {
                Pair< String, File > pair = pairs.get(i);
                mBitmapFiles.put(pair.first, pair.second);
            }

            // TODO: 2018-05-06 如果网络获取失败 或者 某个读取失败-->没有处理
        }
    }


    public ArrayMap< String, File > getBitmapFiles() {

        return mBitmapFiles;
    }
}
