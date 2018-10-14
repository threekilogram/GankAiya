package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import tech.threekilogram.depository.cache.json.JsonLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      private static JsonLoader<GankCategoryItem> mAndroidLoader;

      public static void init ( ) {

            if( mAndroidLoader == null ) {
                  mAndroidLoader = new JsonLoader<>(
                      30,
                      FileManager.getAndroidJsonFile(),
                      GankCategoryItem.class
                  );
            }
      }
}
