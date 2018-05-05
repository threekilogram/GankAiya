package com.example.wuxio.gankexamples.main;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.gank.CategoryRunnable;
import com.example.wuxio.gankexamples.gank.beauty.BeautyQueryRunnable;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager {

    private static final String TAG = "MainManager";

    WeakReference< MainActivity > mRef;


    private MainManager() {

    }


    public void register(MainActivity activity) {

        mRef = new WeakReference<>(activity);
    }


    public static MainManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    @SuppressWarnings("unchecked")
    public void onActivityCreate() {

        ObjectBus bus = new ObjectBus();
        bus.toUnder(new CategoryRunnable(CategoryConstant.BEAUTY, 5, 1, bus))
                .go(new BeautyQueryRunnable(5, 1, bus))
                .run();
    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }
}
