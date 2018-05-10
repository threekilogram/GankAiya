package com.example.wuxio.gankexamples.app;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.file.PreferenceManager;
import com.example.wuxio.gankexamples.gank.history.CacheHistoryRunnable;
import com.example.wuxio.gankexamples.gank.history.GetHistoryJsonRunnable;
import com.example.wuxio.gankexamples.model.HistoryBean;
import com.example.wuxio.gankexamples.model.dao.factory.DaoFactory;

import java.util.List;

/**
 * @author wuxio 2018-05-09:22:00
 */
public class AppManager {

    private static final String TAG = "AppManager";

    private ObjectBus mBus = new ObjectBus();


    public void loadHistoryData() {

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                /* 先从网络读取历史列表 */

                GetHistoryJsonRunnable getHistoryJsonRunnable = new GetHistoryJsonRunnable();
                getHistoryJsonRunnable.run();
                List< HistoryBean > beans = getHistoryJsonRunnable.getHistoryBeans();

                if (beans == null) {

                    // TODO: 2018-05-10 获取json 失败

                    return;
                }

                /* 如果本地数据库没有历史纪录,直接插入历史记录,有的话比较网络和本地,有新的去加载 */

                long latest = DaoFactory.getHistoryDao().findLatest();
                if (latest == -1) {

                    /* 黑没有数据,获取数据插入表中 */

                    DaoFactory.getHistoryDao().insert(beans);
                    run();

                } else {

                    /* 有数据 */
                    HistoryBean latestFromGank = beans.get(0);
                    if (latestFromGank.date > latest) {

                        // TODO: 2018-05-10 找出大于的数据列表去加载

                    } else {

                        long gankDay = PreferenceManager.getAlreadyCachedGankDay();
                        if (gankDay == -1) {

                            /* 从最新的开始缓存 */
                            CacheHistoryRunnable cacheHistoryRunnable = new CacheHistoryRunnable(beans);
                            cacheHistoryRunnable.run();

                        } else {

                            /*  */

                        }
                    }

                }
            }
        }).run();

    }

    //============================ singleTon ============================


    private AppManager() {

    }


    public static AppManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final AppManager INSTANCE = new AppManager();
    }
}
