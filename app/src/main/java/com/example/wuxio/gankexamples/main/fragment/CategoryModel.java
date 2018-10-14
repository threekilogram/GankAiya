package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import tech.threekilogram.depository.cache.json.JsonLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      private static JsonLoader<GankCategoryItem> sAndroidLoader;
      private static LocalCategoryBean            sAndroidBean = new LocalCategoryBean();

      public static void init ( ) {

            if( sAndroidLoader == null ) {
            }
      }
}
