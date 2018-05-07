package com.example.wuxio.gankexamples.gank.beauty;

import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.dao.factory.DaoFactory;
import com.example.wuxio.gankexamples.model.ResultsBean;

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

        List< ResultsBean > resultsBeans = DaoFactory
                .getCategoryDao(CategoryConstant.BEAUTY)
                .query(CategoryConstant.BEAUTY, count, page);

        /* 找出ImageURl */

        final int size = resultsBeans.size();
        mUrls = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ResultsBean bean = resultsBeans.get(i);
            mUrls.add(bean.url);
        }
    }


    public List< String > getUrls() {

        return mUrls;
    }
}
