package com.example.wuxio.gankexamples.splash;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;

/**
 * @author wuxio 2018-05-10:10:12
 */
public class SplashManager extends BaseActivityManager< SplashActivity > {

    //============================ singleTon ============================


    private SplashManager() {

    }


    public static SplashManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final SplashManager INSTANCE = new SplashManager();
    }

    //============================ care ============================

    ObjectBus mBus = new ObjectBus();


    @Override
    public void onActivityCreate() {

    }


    /**
     * load background image from net
     */
    public void loadLogoImage() {

    }

}
