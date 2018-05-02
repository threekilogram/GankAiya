package com.example.wuxio.gankexamples.beauty.model;

import com.example.wuxio.gankexamples.model.CategoryResult;
import com.example.wuxio.gankexamples.utils.date.Format;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-02:16:24
 */
public class Convert {

    public static List< BeautyEntity > map(List< CategoryResult.ResultsBean > beanList) {

        int size = beanList.size();
        ArrayList< BeautyEntity > result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            CategoryResult.ResultsBean bean = beanList.get(i);
            long publishDate = Format.format(bean.publishedAt);
            BeautyEntity entity = new BeautyEntity(publishDate, bean.url);
            result.add(entity);
        }

        return result;
    }

}
