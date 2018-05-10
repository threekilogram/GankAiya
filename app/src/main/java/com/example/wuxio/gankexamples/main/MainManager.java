package com.example.wuxio.gankexamples.main;

import com.example.wuxio.gankexamples.BaseActivityManager;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager extends BaseActivityManager< MainActivity > {

    private static final String TAG = "MainManager";

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

    }
}
