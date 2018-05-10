package com.example.wuxio.gankexamples.model.dao;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.model.GankCategoryBean;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * @author wuxio 2018-05-10:21:10
 */
public class DaoManager {

    //============================ singleTon ============================


    private DaoManager() {

        BoxStore boxStore = App.INSTANCE.getBoxStore();
        mCategoryBeanBox = boxStore.boxFor(GankCategoryBean.class);
    }


    public static DaoManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final DaoManager INSTANCE = new DaoManager();
    }

    //============================ initDataBase ============================

    private final Box< GankCategoryBean > mCategoryBeanBox;
    
}
