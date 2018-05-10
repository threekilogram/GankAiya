package com.example.wuxio.gankexamples.gank.beauty;

import com.example.wuxio.gankexamples.constant.GankCategory;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.dao.factory.DaoFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-05:11:10
 */
public class BeautyQueryRunnable implements Runnable {

    private static final String TAG = "BeautyQueryRunnable";

    private int            count;
    private int            page;
    private List< String > mUrls;


    public BeautyQueryRunnable(int count, int page) {

        this.count = count;
        this.page = page;
    }


    @Override
    public void run() {

        /* 从分类数据读取分类entity */

        List< GankCategoryBean > resultsBeans = DaoFactory
                .getCategoryDao(GankCategory.BEAUTY)
                .query(GankCategory.BEAUTY, count, page);

        /* 找出ImageURl */

        final int size = resultsBeans.size();
        mUrls = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            GankCategoryBean bean = resultsBeans.get(i);
            mUrls.add(bean.url);
        }
    }


    public List< String > getUrls() {

        return mUrls;
    }
}
