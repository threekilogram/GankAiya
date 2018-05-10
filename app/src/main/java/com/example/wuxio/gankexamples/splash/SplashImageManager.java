package com.example.wuxio.gankexamples.splash;

import com.example.wuxio.gankexamples.constant.ImageUrl;

/**
 * @author wuxio 2018-05-10:12:34
 */
public class SplashImageManager {

    //============================ singleTon ============================


    private SplashImageManager() {

    }


    public static SplashImageManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final SplashImageManager INSTANCE = new SplashImageManager();
    }

    //============================ url ============================


    public static String getUrl() {

        return ImageUrl.TRANSITION_URLS[1];
    }
}
