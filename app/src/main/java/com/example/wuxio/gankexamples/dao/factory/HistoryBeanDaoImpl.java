package com.example.wuxio.gankexamples.dao.factory;

import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.dao.history.HistoryBeanDao;
import com.example.wuxio.gankexamples.model.HistoryBean;
import com.example.wuxio.gankexamples.model.HistoryBean_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * @author wuxio 2018-05-10:6:26
 */
public class HistoryBeanDaoImpl implements HistoryBeanDao {

    //============================ singleTon ============================

    private final Box< HistoryBean > mHistoryBeanBox;


    private HistoryBeanDaoImpl() {

        mHistoryBeanBox = App.INSTANCE.getBoxStore().boxFor(HistoryBean.class);
    }


    public static HistoryBeanDaoImpl getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final HistoryBeanDaoImpl INSTANCE = new HistoryBeanDaoImpl();
    }

    //============================ care ============================


    @Override
    public HistoryBean query(long date) {

        return mHistoryBeanBox.get(date);
    }


    @Override
    public List< HistoryBean > query(long[] dates) {

        return mHistoryBeanBox.get(dates);
    }


    @Override
    public long findLatest() {

        Query< HistoryBean > build = mHistoryBeanBox.query().orderDesc(HistoryBean_.date).build();
        HistoryBean first = build.findFirst();

        if (first != null) {

            return first.date;
        } else {
            return -1;
        }
    }


    @Override
    public void insert(List< HistoryBean > historyBeans) {

        mHistoryBeanBox.put(historyBeans);
    }
}
