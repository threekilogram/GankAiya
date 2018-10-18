package com.example.wuxio.gankexamples.model;

import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.DateUtil;
import com.example.wuxio.gankexamples.utils.NetWork;
import java.io.File;
import java.util.Date;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.model.cache.json.ObjectLoader;
import tech.threekilogram.model.stream.StreamLoader;

/**
 * @author Liujin 2018-10-15:7:48
 */
public class Model {

      /**
       * 从本地文件构建{@link LocalCategoryBean}
       */
      public static LocalCategoryBean buildLocalBeanFromFile (
          String category,
          File localFile,
          File latestJsonFile ) {

            LocalCategoryBean r = ObjectLoader.loadFromFile( localFile, LocalCategoryBean.class );

            /* 从网络获取最新的数据 */
            if( NetWork.hasNetwork() ) {
                  int i = Model.downLoadLatestJson(
                      category,
                      r,
                      latestJsonFile,
                      localFile
                  );
            }

            return r;
      }

      /**
       * 下载最新的福利数据,如果没有缓存过那么添加到local缓存
       */
      public static int downLoadLatestJson (
          String category, LocalCategoryBean localBean, File latestJsonFile, File localFile ) {

            /* 下载最新的json数据 */
            Date date = DateUtil.getDate( localBean.getStartDate() );
            File latestJson = BeanLoader.downLoadLatestJson( category, latestJsonFile, date );

            int size = localBean.getUrls().size();
            JsonUtil.parserLatestJson( latestJson, date, localBean );

            int newSize = localBean.getUrls().size();
            if( newSize > size ) {
                  /* 缓存最新数据到本地 */
                  PoolExecutor.execute( ( ) -> {
                        ObjectLoader.toFile( localFile, localBean, LocalCategoryBean.class );
                  } );
            }

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

            StreamLoader.downLoad( url, jsonFile );

            LocalCategoryBean bean = JsonUtil.parseJsonToLocalBean( jsonFile );

            PoolExecutor.execute( ( ) -> {

                  /* 缓存最新数据 */
                  ObjectLoader.toFile( localBeanFile, bean, LocalCategoryBean.class );
            } );

            return bean;
      }
}
