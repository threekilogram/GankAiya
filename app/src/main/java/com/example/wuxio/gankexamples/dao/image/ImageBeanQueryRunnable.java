package com.example.wuxio.gankexamples.dao.image;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.dao.factory.DaoFactory;
import com.example.wuxio.gankexamples.model.ImageBean;
import com.example.wuxio.gankexamples.model.ResultsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-05:11:10
 */
public class ImageBeanQueryRunnable implements Runnable {

    private int       count;
    private int       page;
    private ObjectBus mBus;

    public static final String BUS_KEY_BEANS = "imageBeans_list";


    public ImageBeanQueryRunnable(int count, int page, ObjectBus bus) {

        this.count = count;
        this.page = page;
        mBus = bus;
    }


    @Override
    public void run() {

        /* 从分类数据读取分类entity */

        List< ResultsBean > resultsBeans = DaoFactory
                .getCategoryDao(CategoryConstant.BEAUTY)
                .query(CategoryConstant.BEAUTY, count, page);

        /* 找出ImageURl */

        final int size = resultsBeans.size();
        List< String > urls = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ResultsBean bean = resultsBeans.get(i);
            urls.add(bean.url);
        }

        /* 从图片数据库根据url list读取图片entity */

        ImageBeanDao imageDao = DaoFactory.getImageDao();
        List< ImageBean > imageBeans = imageDao.query(urls);

        /* 找出没有该url对应的entity */

        for (int i = 0; i < size; i++) {
            ImageBean imageBean = imageBeans.get(i);
            if (imageBean != null) {
                urls.remove(imageBean.url);
            }
        }

        /* 为url 没有对应entity的数据项插入entity */

        int noEntitySize = urls.size();

        mBus.takeAs(imageBeans, BUS_KEY_BEANS);
    }
}
