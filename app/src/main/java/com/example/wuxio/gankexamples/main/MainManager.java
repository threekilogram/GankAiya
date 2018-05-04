package com.example.wuxio.gankexamples.main;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.beauty.BeautyJsonRunnable;

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

        ObjectBus bus = new ObjectBus();
        bus.toUnder(new BeautyJsonRunnable(5, 1)).run();
    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }
}
