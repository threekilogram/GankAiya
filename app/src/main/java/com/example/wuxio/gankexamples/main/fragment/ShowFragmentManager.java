package com.example.wuxio.gankexamples.main.fragment;

import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.objectbus.message.Messengers;
import com.example.objectbus.message.OnMessageReceiveListener;
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

        bus.toUnder(() -> {

            List< GankCategoryBean > categoryBeans = ModelManager.getInstance().loadCategory(category);

            bus.take(categoryBeans, "temp");
        }).toMain(() -> {

            List< GankCategoryBean > categoryBeans = (List< GankCategoryBean >) bus.getOff("temp");

            try {
                get().dataReady(categoryBeans);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            BusStation.recycle(bus);
        }).run();
    }

    //============================ load more ============================


    public void loadMore(String category, OnMessageReceiveListener listener) {

        ObjectBus bus = BusStation.callNewBus();
        bus.toUnder(new Runnable() {
            @Override
            public void run() {

                ModelManager.getInstance().loadCategoryMore(category);
                Messengers.send(11, listener);
                BusStation.recycle(bus);
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
