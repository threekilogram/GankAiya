package com.example.wuxio.gankexamples.dao.factory;

import com.example.wuxio.gankexamples.dao.category.CategoryDao;
import com.example.wuxio.gankexamples.dao.image.ImageBeanDao;

/**
 * @author wuxio 2018-05-05:8:11
 */
public class DaoFactory {

    public static CategoryDao getCategoryDao(String type) {

        if (type == "福利") {
            return CategoryDaoImpl.getInstance();
        }

        return null;
    }


    public static ImageBeanDao getImageDao() {

        return ImageBeanDaoImpl.getInstance();
    }
}
