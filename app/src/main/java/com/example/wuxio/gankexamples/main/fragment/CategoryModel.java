package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import tech.threekilogram.depository.cache.json.JsonLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      private static JsonLoader<GankCategoryItem> sAndroidLoader;
      private static LocalCategoryBean            sAndroidBean = new LocalCategoryBean();

      private static final int sCount = 30;

      public static void init ( ) {

            if( sAndroidLoader == null ) {
                  sAndroidLoader = new JsonLoader<>(
                      sCount,
                      FileManager.getAndroidFile(),
                      GankCategoryItem.class
                  );
            }
            buildLocalAndroidBean();
      }

      private static void buildLocalAndroidBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  File localAndroidBeanFile = FileManager.getLocalAndroidBeanFile();
                  if( localAndroidBeanFile.exists() ) {
                        /* 从本地缓存构建 */
                  } else {
                        /* 从网络构建 */
                        if( NetWork.hasNetwork() ) {

                        } else {

                        }
                  }
            } );
      }
}
