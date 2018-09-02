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

      public static final String BEAUTY         = "福利";
      public static final String ANDROID        = "Android";
      public static final String IOS            = "ios";
      public static final String TAKE_REST      = "休息视频";
      public static final String EXTRA_RESOURCE = "拓展资源";
      public static final String FOUNT          = "前端";
      public static final String ALL            = "all";

      @StringDef(value = { BEAUTY, ANDROID, IOS, TAKE_REST, EXTRA_RESOURCE, FOUNT, ALL })
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

      public static String splashImageUrl ( ) {

            String encode = null;
            try {
                  encode = URLEncoder.encode( BEAUTY, "UTF-8" );
            } catch(UnsupportedEncodingException e) {
                  e.printStackTrace();
            }
            return category( encode, 1, 1 );
      }
}
