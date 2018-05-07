package com.example.wuxio.gankexamples.main;

import android.util.ArrayMap;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.ActivityManager;
import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.gank.CategoryRunnable;
import com.example.wuxio.gankexamples.gank.beauty.ImageLoadRunnable;
import com.example.wuxio.gankexamples.model.ResultsBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager extends ActivityManager< MainActivity > {

    private static final String TAG = "MainManager";
    ObjectBus mBus = new ObjectBus();

    //============================ singleTon ============================


    private MainManager() {

    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }


    public static MainManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreate() {

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                CategoryRunnable categoryRunnable = new CategoryRunnable(
                        CategoryConstant.BEAUTY,
                        5,
                        1
                );
                categoryRunnable.run();
                List< ResultsBean > resultsBeans = categoryRunnable.getResultsBeans();

                int size = resultsBeans.size();
                List< String > urls = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    ResultsBean bean = resultsBeans.get(i);
                    urls.add(bean.url);
                }

                ImageLoadRunnable imageLoadRunnable = new ImageLoadRunnable(urls);
                imageLoadRunnable.run();
                ArrayMap< String, File > bitmapFiles = imageLoadRunnable.getBitmapFiles();

                mBus.take(bitmapFiles, "bitmapFiles");
                mBus.take(urls, "urls");

            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

                MainActivity activity = mReference.get();
                if (activity != null) {
                    ArrayMap< String, File > bitmapFiles =
                            (ArrayMap< String, File >) mBus.getOff("bitmapFiles");
                    List< String > urls = (List< String >) mBus.getOff("urls");
                    activity.setBannerImageData(urls, bitmapFiles);
                }

            }
        }).run();

    }

}
