package com.example.wuxio.gankexamples.main;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager {

    WeakReference< MainActivity > mRef;


    private MainManager() {

    }


    public void register(MainActivity activity) {

        mRef = new WeakReference<>(activity);
    }


    public static MainManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    public void onActivityCreate() {

    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }
}
