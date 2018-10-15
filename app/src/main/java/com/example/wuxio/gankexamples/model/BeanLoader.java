package com.example.wuxio.gankexamples.model;

import android.util.Log;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import java.io.File;
import java.util.Date;
import tech.threekilogram.depository.stream.StreamLoader;

/**
 * @author Liujin 2018-10-07:19:24
 */
public class BeanLoader {

      private static final String TAG = BeanLoader.class.getSimpleName();

      /**
       * 初始化变量
       */
      public static void init ( ) { }

      /**
       * 获取最新的一条分类数据数据
       */
      public static GankCategory loadLatestCategoryJson ( String latestUrl ) {

            return StreamLoader
                .loadJsonFromNet( latestUrl, GankCategory.class );
      }

      /**
       * 获取最新的福利数据
       */
      public static File downLoadLatestJson (
          String category, File latestJsonFile, Date startDate ) {

            final int page = 1;
            int count = 20;
            String url = GankUrl.category( category, count, page );

            if( latestJsonFile.exists() ) {
                  latestJsonFile.delete();
            }

            Log.e( TAG, "downLoadLatestJson : 下载最新的json数据中... " + category );

            /* 下载最新的数据 */
            StreamLoader.downLoad( url, latestJsonFile );

            /* 如果最新的数据不够,那么增加数量,继续下载 */
            while( JsonUtil.parserJsonToGetIsNeedMore( category, latestJsonFile, startDate ) ) {
                  boolean delete = latestJsonFile.delete();
                  count += count;
                  url = GankUrl.category( category, count, page );
                  StreamLoader.downLoad( url, latestJsonFile );
            }

            Log.e( TAG, "downLoadLatestJson : 下载最新的json数据完成 " + category + " " + count );

            return latestJsonFile;
      }
}
