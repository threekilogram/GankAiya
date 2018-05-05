package com.example.wuxio.gankexamples.dao.category;

import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.ResultsBean;
import com.example.wuxio.gankexamples.model.ResultsBean_;

import java.util.List;

import io.objectbox.Box;

/**
 * @author wuxio 2018-05-05:8:12
 */
class CategoryDaoImpl implements CategoryDao {

    //============================ singleTon ============================


    private CategoryDaoImpl() {

        mResultsBeanBox = App.INSTANCE.getBoxStore().boxFor(ResultsBean.class);
    }


    public static CategoryDaoImpl getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final CategoryDaoImpl INSTANCE = new CategoryDaoImpl();
    }

    //============================ dao ============================

    private final Box< ResultsBean > mResultsBeanBox;


    @Override
    public void insert(List< ResultsBean > resultsBeans) {

        mResultsBeanBox.put(resultsBeans);
    }


    @Override
    public List< ResultsBean > getAll() {

        return mResultsBeanBox.getAll();
    }


    @Override
    public List< ResultsBean > query(String type, int count, int page) {

        int offset = count * (page - 1);
        if (offset < 0) {
            offset = 0;
        }

        List< ResultsBean > beans = mResultsBeanBox.query()
                .equal(ResultsBean_.type, type)
                .orderDesc(ResultsBean_.id)
                .build()
                .find(offset, count);

        return beans;
    }

}
