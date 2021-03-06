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

      public static final String ANDROID         = "Android";
      public static final String APP             = "App";
      public static final String IOS             = "iOS";
      public static final String FRONT           = "前端";
      public static final String RECOMMEND       = "瞎推荐";
      public static final String EXTRA_RESOURCES = "拓展资源";
      public static final String REST_VIDEO      = "休息视频";
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
      public static String category ( String category, int count, int page ) {

            try {
                  category = URLEncoder.encode( category, "UTF-8" );
                  return String.format(
                      Locale.ENGLISH,
                      "https://gank.io/api/data/%s/%d/%d",
                      category,
                      count,
                      page
                  );
            } catch(UnsupportedEncodingException e) {
                  e.printStackTrace();
            }

            return null;
      }

      public static String beautyAllUrl ( ) {

            return category( BEAUTY, Integer.MAX_VALUE, 1 );
      }

      public static String androidAllUrl ( ) {

            return category( ANDROID, Integer.MAX_VALUE, 1 );
      }
}
