package com.example.wuxio.gankexamples.model;

import java.util.List;

/**
 * @author wuxio 2018-05-05:8:30
 */
public class ResultBeanSetIdUtil {

    /**
     * @param beans 为bean 设置ID
     */
    public static void setID(List< ResultsBean > beans) {

        final int size = beans.size();
        for (int i = 0; i < size; i++) {
            ResultsBean bean = beans.get(i);
            bean.id = bean.publishedAt;
        }
    }
}
