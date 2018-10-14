package com.example.wuxio.gankexamples.model;

import android.support.annotation.StringDef;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-08-15
 * @time: 17:21
 */
public class GankUrl {


      /* 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all */

      public static final String REST_VIDEO      = "休息视频";
      public static final String EXTRA_RESOURCES = "拓展资源";
      public static final String FRONT           = "前端";
      public static final String ANDROID         = "Android";
      public static final String RECOMMEND       = "瞎推荐";
      public static final String APP             = "App";
      public static final String IOS             = "iOS";
      public static final String BEAUTY          = "福利";
      public static final String ALL             = "all";

      public static final String[] CATEGORY = {
          ANDROID,
          APP,
          IOS,
          FRONT,
          RECOMMEND,
          EXTRA_RESOURCES,
          REST_VIDEO
      };

      @StringDef(value = { BEAUTY, ANDROID, IOS, REST_VIDEO, EXTRA_RESOURCES, FRONT, ALL })
      public @interface CategoryValue { }

      /**
       * 获取分类Url
       *
       * @param category 分类
       * @param count count
       * @param page page
       *
       * @return url
       */
      public static String category ( @CategoryValue String category, int count, int page ) {

            return String.format(
                Locale.ENGLISH,
                "https://gank.io/api/data/%s/%d/%d",
                category,
                count,
                page
            );
      }

      public static String beautyUrl ( int count, int page ) {

            String encode = null;
            try {
                  encode = URLEncoder.encode( BEAUTY, "UTF-8" );
            } catch(UnsupportedEncodingException e) {
                  e.printStackTrace();
            }
            return category( encode, count, page );
      }

      public static String beautyAllUrl ( ) {

            return beautyUrl( Integer.MAX_VALUE, 1 );
      }

      public static String beautyLatestUrl ( ) {

            return beautyUrl( 1, 1 );
      }

      public static String dayUrl ( String date ) {

            String year = date.substring( 0, 4 );
            String month = date.substring( 5, 7 );
            String day = date.substring( 8 );

            return String.format(
                Locale.ENGLISH,
                "https://gank.io/api/day/%s/%s/%s",
                year,
                month,
                day
            );
      }
}
