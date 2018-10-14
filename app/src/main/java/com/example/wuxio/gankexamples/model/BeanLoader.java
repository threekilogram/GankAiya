package com.example.wuxio.gankexamples.model;

import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.splash.SplashModel;
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
       * 获取最新的beauty分类的最新的一条数据,{@link SplashModel#setSplashImage()}
       */
      public static String loadLatestBeautyUrl ( ) {

            /* 从网络加载最新的数据 */
            String latestUrl = GankUrl.beautyLatestUrl();
            GankCategory gankCategory = StreamLoader
                .loadJsonFromNet( latestUrl, GankCategory.class );

            String result = gankCategory.getResults().get( 0 ).getUrl();
            Log.e(
                TAG,
                "loadLatestBeautyUrl : 获取到的最新福利url: " + result
            );
            return result;
      }

      /**
       * 获取最新的福利数据
       */
      public static File downLoadLatestBeautyJson ( Date startDate ) {

            final int page = 1;
            int count = 20;
            final String beautyUrl = GankUrl.beautyUrl( count, page );

            File jsonFile = FileManager.getLatestBeautyJsonFile();
            if( jsonFile.exists() ) {
                  jsonFile.delete();
            }

            Log.e( TAG, "downLoadLatestBeautyJson : 下载最新的福利数据中..." );

            /* 下载最新的数据 */
            StreamLoader.downLoad( beautyUrl, jsonFile );

            /* 如果最新的数据不够,那么增加数量,继续下载 */
            while( JsonUtil.parserBeautyJsonToGetIsNeedMoreLatest( jsonFile, startDate ) ) {
                  boolean delete = jsonFile.delete();
                  count += count;
                  String beautyUrl1 = GankUrl.beautyUrl( count, page );
                  StreamLoader.downLoad( beautyUrl1, jsonFile );
            }

            Log.e( TAG, "downLoadLatestBeautyJson : 下载最新的福利数据完成 " + count );

            return jsonFile;
      }
}
