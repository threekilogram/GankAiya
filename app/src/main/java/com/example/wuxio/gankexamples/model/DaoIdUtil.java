package com.example.wuxio.gankexamples.model;

import com.example.wuxio.gankexamples.utils.Url2Long;

import java.util.List;

/**
 * @author wuxio 2018-05-05:8:30
 */
public class DaoIdUtil {

    /**
     * @param beans 为bean 设置ID
     */
    public static void setResultsBeanID(List< ResultsBean > beans) {

        final int size = beans.size();
        for (int i = 0; i < size; i++) {
            ResultsBean bean = beans.get(i);
            bean.id = bean.publishedAt;
        }
    }


    /**
     * @param imageBeans 为 {@link ImageBean} 设置ID
     */
    public static void setImageBeanID(List< ImageBean > imageBeans) {

        final int size = imageBeans.size();

        for (int i = 0; i < size; i++) {
            ImageBean imageBean = imageBeans.get(i);
            String url = imageBean.url;
            imageBean.id = Url2Long.to(url);
        }
    }
}
