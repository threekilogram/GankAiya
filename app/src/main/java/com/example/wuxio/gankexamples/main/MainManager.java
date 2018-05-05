package com.example.wuxio.gankexamples.main;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.beauty.CategoryBeautyRunnable;
import com.example.wuxio.gankexamples.dao.category.CategoryDaoFactory;
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

                        List< ResultsBean > query = CategoryDaoFactory
                                .getCategoryDao()
                                .query("福利", 5, 1);

                        String url = query.get(0).url;


                    }
                })
                .run();
    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }
}
