package com.example.wuxio.gankexamples.model.dao.factory;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.model.GankHistoryBean;
import com.example.wuxio.gankexamples.model.GankHistoryBean_;
import com.example.wuxio.gankexamples.model.dao.history.HistoryBeanDao;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * @author wuxio 2018-05-10:6:26
 */
public class HistoryBeanDaoImpl implements HistoryBeanDao {

    //============================ singleTon ============================

    private final Box< GankHistoryBean > mHistoryBeanBox;


    private HistoryBeanDaoImpl() {

        mHistoryBeanBox = App.INSTANCE.getBoxStore().boxFor(GankHistoryBean.class);
    }


    public static HistoryBeanDaoImpl getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final HistoryBeanDaoImpl INSTANCE = new HistoryBeanDaoImpl();
    }

    //============================ care ============================


    @Override
    public GankHistoryBean query(long date) {

        return mHistoryBeanBox.get(date);
    }


    @Override
    public List< GankHistoryBean > query(long[] dates) {

        return mHistoryBeanBox.get(dates);
    }


    @Override
    public long findLatest() {

        Query< GankHistoryBean > build = mHistoryBeanBox.query().orderDesc(GankHistoryBean_.date).build();
        GankHistoryBean first = build.findFirst();

        if (first != null) {

            return first.date;
        } else {
            return -1;
        }

    }


    @Override
    public void insert(List< GankHistoryBean > historyBeans) {

        mHistoryBeanBox.put(historyBeans);
    }
}
