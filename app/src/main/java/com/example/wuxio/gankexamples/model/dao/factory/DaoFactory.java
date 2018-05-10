package com.example.wuxio.gankexamples.model.dao.factory;

import com.example.wuxio.gankexamples.constant.GankCategory;
import com.example.wuxio.gankexamples.model.dao.category.CategoryDao;
import com.example.wuxio.gankexamples.model.dao.image.ImageBeanDao;

/**
 * @author wuxio 2018-05-05:8:11
 */
public class DaoFactory {

    public static CategoryDao getCategoryDao(String type) {

        if (GankCategory.BEAUTY.equals(type)) {
            return CategoryDaoImpl.getInstance();
        }

        return null;
    }


    public static ImageBeanDao getImageDao() {

        return ImageBeanDaoImpl.getInstance();
    }

}
