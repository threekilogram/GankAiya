package com.example.wuxio.gankexamples.dao.category;

/**
 * @author wuxio 2018-05-05:8:11
 */
public class CategoryDaoFactory {

    public static CategoryDao getCategoryDao() {

        return CategoryDaoImpl.getInstance();
    }

}
