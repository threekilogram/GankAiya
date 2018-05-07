package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import android.util.ArrayMap;

import com.example.banner.BannerView;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.ActivityManager;
import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.gank.CategoryRunnable;
import com.example.wuxio.gankexamples.gank.beauty.ImageFileLoadRunnable;
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

    //============================ core ============================


    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreate() {

        BannerView banner = mReference.get().getBanner();
        int width = banner.getWidth();
        int height = banner.getHeight();
        banner = null;

        /* 网络请求最新的5条 福利 数据,从本地加载对应图片,如果没有网络下载到本地,然后解析成bitmap,通知activity设置数据 */

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                /* 从后台准备数据 */

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

                ImageFileLoadRunnable imageFileLoadRunnable = new ImageFileLoadRunnable(
                        urls,
                        bitmapFiles,
                        width,
                        height
                );
                imageFileLoadRunnable.run();
                List< Bitmap > bitmaps = imageFileLoadRunnable.getBitmaps();

                mBus.take(bitmapFiles, "bitmapFiles");
                mBus.take(urls, "urls");
                mBus.take(bitmaps, "bitmaps");

            }
        }).toMain(new Runnable() {

            /* 准备好之后,切换到主线程更新banner */


            @Override
            public void run() {

                MainActivity activity = mReference.get();
                if (activity != null) {

                    List< String > urls = (List< String >) mBus.getOff("urls");
                    ArrayMap< String, File > bitmapFiles =
                            (ArrayMap< String, File >) mBus.getOff("bitmapFiles");
                    List< Bitmap > bitmaps = (List< Bitmap >) mBus.getOff("bitmaps");

                    activity.setBannerImageData(urls, bitmapFiles, bitmaps);
                }
                mBus.clearRunnable();
            }
        }).run();
    }
}
