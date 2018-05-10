package com.example.wuxio.gankexamples.model.dao.factory;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.GankCategoryBean_;
import com.example.wuxio.gankexamples.model.dao.category.CategoryDao;

import java.util.List;

import io.objectbox.Box;

/**
 * @author wuxio 2018-05-05:8:12
 */
class CategoryDaoImpl implements CategoryDao {

    //============================ singleTon ============================


    private CategoryDaoImpl() {

        mResultsBeanBox = App.INSTANCE.getBoxStore().boxFor(GankCategoryBean.class);
    }


    public static CategoryDaoImpl getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final CategoryDaoImpl INSTANCE = new CategoryDaoImpl();
    }

    //============================ dao ============================

    private final Box< GankCategoryBean > mResultsBeanBox;


    @Override
    public void insert(List< GankCategoryBean > resultsBeans) {

        mResultsBeanBox.put(resultsBeans);
    }


    @Override
    public List< GankCategoryBean > getAll() {

        return mResultsBeanBox.getAll();
    }


    @Override
    public List< GankCategoryBean > query(String type, int count, int page) {

        int offset = count * (page - 1);
        if (offset < 0) {
            offset = 0;
        }

        List< GankCategoryBean > beans = mResultsBeanBox.query()
                .equal(GankCategoryBean_.type, type)
                .orderDesc(GankCategoryBean_.id)
                .build()
                .find(offset, count);

        return beans;
    }
}
