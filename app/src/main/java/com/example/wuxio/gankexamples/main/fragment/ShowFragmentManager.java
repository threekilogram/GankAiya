package com.example.wuxio.gankexamples.main.fragment;

import com.example.objectbus.bus.ObjectBus;
import com.example.objectbus.bus.ObjectBusStation;
import com.example.objectbus.message.Messengers;
import com.example.objectbus.message.OnMessageReceiveListener;
import com.example.wuxio.gankexamples.BaseManager;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.ModelManager;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author wuxio 2018-05-13:9:03
 */
public class ShowFragmentManager extends BaseManager< ShowFragment > {

    private static final String TAG = "ShowFragmentManager";

    //============================ ui ============================


    public void loadData(String category) {

        ObjectBus bus = ObjectBusStation.callNewBus();

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
            ObjectBusStation.recycle(bus);
        }).run();
    }

    //============================ load more ============================


    public void loadMore(String category, OnMessageReceiveListener listener) {

        ObjectBus bus = ObjectBusStation.callNewBus();

        WeakReference< OnMessageReceiveListener > listenerRef = new WeakReference<>(listener);

        bus.toUnder(() -> {

            ModelManager.getInstance().loadCategoryMore(category);
            OnMessageReceiveListener ref = listenerRef.get();
            if (ref != null) {

                Messengers.send(11, ref);
            }
            ObjectBusStation.recycle(bus);

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
