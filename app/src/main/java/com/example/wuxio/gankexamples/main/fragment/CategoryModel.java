package com.example.wuxio.gankexamples.main.fragment;

import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import tech.threekilogram.depository.cache.json.JsonLoader;
import tech.threekilogram.depository.stream.StreamLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      private static final String TAG = CategoryModel.class.getSimpleName();

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
            buildLocalBean( GankUrl.ANDROID );
      }

      private static void buildLocalBean ( String type ) {

            if( type.equals( GankUrl.ANDROID ) ) {

                  final File localBean = FileManager.getLocalAndroidBeanFile();
                  final File jsonFile = FileManager.getAndroidJsonFile();
                  final File latestFile = FileManager.getLatestAndroidJsonFile();

                  PoolExecutor.execute( ( ) -> {
                        if( localBean.exists() ) {

                        } else {
                              buildLocalBeanFromNet(
                                  sAndroidBean,
                                  jsonFile,
                                  GankUrl.androidAllUrl()
                              );
                        }
                  } );
            }
      }

      /**
       * 从网络构建BeautiesBean
       */
      private static void buildLocalBeanFromNet (
          LocalCategoryBean localBean, File jsonFile, String url ) {

            if( jsonFile.exists() ) {
                  boolean delete = jsonFile.delete();
            }
            Log.e(
                TAG,
                "buildLocalBean : 从网络下载分类数据.json中: " + url
            );
            StreamLoader.downLoad( url, jsonFile );
            Log.e(
                TAG,
                "buildLocalBean : 从网络下载分类数据.json完成: " + jsonFile
            );
      }
}
