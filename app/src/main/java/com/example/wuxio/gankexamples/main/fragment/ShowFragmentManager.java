package com.example.wuxio.gankexamples.main.fragment;

import android.util.Log;

import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseManager;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.ModelManager;

import java.util.List;

/**
 * @author wuxio 2018-05-13:9:03
 */
public class ShowFragmentManager extends BaseManager< ShowFragment > {

    private static final String TAG = "ShowFragmentManager";


    //============================ ui ============================


    public void loadData(String category) {

        ObjectBus bus = BusStation.callNewBus();

        bus.toUnder(new Runnable() {
            @Override
            public void run() {

                List< GankCategoryBean > categoryBeans = ModelManager.getInstance().loadCategory(category);

                for (GankCategoryBean bean : categoryBeans) {
                    Log.i(TAG, "run:" + bean);
                }
            }
        }).run();
    }

    //============================ singleTon ============================


    private ShowFragmentManager() {

    }


    public static ShowFragmentManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final ShowFragmentManager INSTANCE = new ShowFragmentManager();
    }
}
