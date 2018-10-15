package com.example.wuxio.gankexamples.model;

import android.util.Log;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.DateUtil;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.util.Date;
import tech.threekilogram.depository.cache.json.ObjectLoader;
import tech.threekilogram.depository.stream.StreamLoader;

/**
 * @author Liujin 2018-10-15:7:48
 */
public class Model {

      private static final String TAG = Model.class.getSimpleName();

      /**
       * 从本地文件构建{@link LocalCategoryBean}
       */
      public static LocalCategoryBean buildLocalBeanFromFile (
          String category,
          File localFile,
          File latestJsonFile ) {

            Log.e( TAG, "buildLocalBean : 从本地缓存构建localBean中 " + localFile );
            LocalCategoryBean r = ObjectLoader.loadFromFile( localFile, LocalCategoryBean.class );
            Log.e( TAG, "buildLocalBeanFromFile : 从本地缓存构建localBean完成 " + r.getUrls().size() );

            /* 从网络获取最新的数据 */
            if( NetWork.hasNetwork() ) {
                  int i = Model.downLoadLatestJson(
                      category,
                      r,
                      latestJsonFile,
                      localFile
                  );
                  Log.e( TAG, "buildLocalBeanFromFile : 从网络添加最新的数据到本地localBean完成 " + i );
            }

            return r;
      }

      /**
       * 下载最新的福利数据,如果没有缓存过那么添加到local缓存
       */
      public static int downLoadLatestJson (
          String category, LocalCategoryBean localBean, File latestJsonFile, File localFile ) {

            Date date = DateUtil.getDate( localBean.getStartDate() );
            File jsonFile = BeanLoader.downLoadLatestJson( category, latestJsonFile, date );

            int size = localBean.getUrls().size();
            JsonUtil.parserLatestJson( jsonFile, date, localBean );
            int newSize = localBean.getUrls().size();
            if( newSize > size ) {
                  /* 缓存最新数据到本地 */
                  PoolExecutor.execute( ( ) -> {
                        ObjectLoader.toFile( localFile, localBean, LocalCategoryBean.class );
                  } );
            }
            boolean delete = jsonFile.delete();
            return newSize - size;
      }

      /**
       * 从网络构建BeautiesBean
       */
      public static LocalCategoryBean buildLocalBeanFromNet (
          String url,
          File jsonFile,
          File localBeanFile ) {

            if( jsonFile.exists() ) {
                  boolean delete = jsonFile.delete();
            }
            Log.e(
                TAG,
                "buildLocalBeanFromNet : 从网络下载.json中: " + url
            );
            StreamLoader.downLoad( url, jsonFile );
            Log.e(
                TAG,
                "buildLocalBeanFromNet : 从网络下载.json完成: " + jsonFile
            );

            LocalCategoryBean bean = JsonUtil.parseJsonToLocalBean( jsonFile );
            Log.e( TAG, "buildLocalBeanFromNet : 从网络构建localBean完成 " );

            PoolExecutor.execute( ( ) -> {

                  /* 缓存最新数据 */
                  ObjectLoader.toFile( localBeanFile, bean, LocalCategoryBean.class );
                  Log.e(
                      TAG,
                      "buildLocalBeanFromNet : 缓存localBean到文件完成 " + localBeanFile.exists() + " "
                          + localBeanFile
                  );
            } );

            return bean;
      }
}
