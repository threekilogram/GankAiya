package com.example.wuxio.gankexamples.main;

import com.example.wuxio.gankexamples.beauty.BeautyManager;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager {

    WeakReference< MainActivity > mRef;


    public MainManager(MainActivity mainActivity) {

        mRef = new WeakReference<>(mainActivity);
    }


    public void onActivityCreate() {

        BeautyManager instance = BeautyManager.getInstance();
        instance.getBeauty(5, 1);
    }

}
