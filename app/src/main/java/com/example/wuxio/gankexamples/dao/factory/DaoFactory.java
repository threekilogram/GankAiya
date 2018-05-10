package com.example.wuxio.gankexamples.dao.factory;

import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.dao.category.CategoryDao;
import com.example.wuxio.gankexamples.dao.history.HistoryBeanDao;
import com.example.wuxio.gankexamples.dao.image.ImageBeanDao;

/**
 * @author wuxio 2018-05-05:8:11
 */
public class DaoFactory {

    public static CategoryDao getCategoryDao(String type) {

        if (CategoryConstant.BEAUTY.equals(type)) {
            return CategoryDaoImpl.getInstance();
        }

        return null;
    }


    public static ImageBeanDao getImageDao() {

        return ImageBeanDaoImpl.getInstance();
    }


    public static HistoryBeanDao getHistoryDao() {

        return HistoryBeanDaoImpl.getInstance();
    }
}
