package com.example.wuxio.gankexamples.main;

import android.util.Log;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.beauty.CategoryBeautyRunnable;
import com.example.wuxio.gankexamples.dao.CategoryDaoFactory;
import com.example.wuxio.gankexamples.model.ResultsBean;

import java.lang.ref.WeakReference;
import java.util.List;

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


    public void onActivityCreate() {

        ObjectBus bus = new ObjectBus();
        bus.toUnder(new CategoryBeautyRunnable(5, 1, bus))
                .go(new Runnable() {
                    @Override
                    public void run() {

                        List< ResultsBean > all = CategoryDaoFactory.getCategoryDao().getAll();
                        Log.i(TAG, "run:" + all);
                    }
                })
                .run();
    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }
}
