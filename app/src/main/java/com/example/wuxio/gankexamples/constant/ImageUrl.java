package com.example.wuxio.gankexamples.constant;

import java.util.ArrayList;

/**
 * @author wuxio
 */
public class ImageUrl {

      /**
       * 电影栏头部的图片
       */
      public static final String ONE_URL_01 = "http://ojyz0c8un.bkt.clouddn.com/one_01.png";

      /**
       * 头像
       */
      public static final String IC_AVATAR = "http://ojyz0c8un.bkt.clouddn.com/ic_avatar.png";

      /**
       * 2张图的随机图
       */
      private static final String   HOME_TWO_01   = "http://ojyz0c8un.bkt.clouddn.com/home_two_01.png";
      private static final String   HOME_TWO_02   = "http://ojyz0c8un.bkt.clouddn.com/home_two_02.png";
      private static final String   HOME_TWO_03   = "http://ojyz0c8un.bkt.clouddn.com/home_two_03.png";
      private static final String   HOME_TWO_04   = "http://ojyz0c8un.bkt.clouddn.com/home_two_04.png";
      private static final String   HOME_TWO_05   = "http://ojyz0c8un.bkt.clouddn.com/home_two_05.png";
      private static final String   HOME_TWO_06   = "http://ojyz0c8un.bkt.clouddn.com/home_two_06.png";
      private static final String   HOME_TWO_07   = "http://ojyz0c8un.bkt.clouddn.com/home_two_07.png";
      private static final String   HOME_TWO_08   = "http://ojyz0c8un.bkt.clouddn.com/home_two_08.png";
      private static final String   HOME_TWO_09   = "http://ojyz0c8un.bkt.clouddn.com/home_two_09.png";
      public static final  String[] HOME_TWO_URLS = new String[]{
          HOME_TWO_01, HOME_TWO_02, HOME_TWO_03, HOME_TWO_04
          , HOME_TWO_05, HOME_TWO_06, HOME_TWO_07, HOME_TWO_08
          , HOME_TWO_09
      };

      /**
       * 一张图的随机图
       */
      private static final String HOME_ONE_1 = "http://ojyz0c8un.bkt.clouddn.com/home_one_1.png";
      /**
       * 1 -- 23
       */
      private static final String HOME_SIX_1 = "http://ojyz0c8un.bkt.clouddn.com/home_six_1.png";
      private static ArrayList<String> oneList;
      /**
       * 一张图的随机图
       */
      public static final String[] HOME_ONE_URLS = new String[]{
          getOneUrl().get( 0 ), getOneUrl().get( 1 ), getOneUrl().get( 2 ), getOneUrl().get( 3 )
          , getOneUrl().get( 4 ), getOneUrl().get( 5 ), getOneUrl().get( 6 ), getOneUrl().get( 7 )
          , getOneUrl().get( 8 ), getOneUrl().get( 9 ), getOneUrl().get( 10 ), getOneUrl().get( 11 )
      };

      //-----------------------------------------------------------------------------
      private static ArrayList<String> sixList;
      /**
       * 六图的随机图
       */
      public static final String[] HOME_SIX_URLS = new String[]{
          getSixUrl().get( 0 ), getSixUrl().get( 1 ), getSixUrl().get( 2 ), getSixUrl().get( 3 )
          , getSixUrl().get( 4 ), getSixUrl().get( 5 ), getSixUrl().get( 6 ), getSixUrl().get( 7 )
          , getSixUrl().get( 8 ), getSixUrl().get( 9 ), getSixUrl().get( 10 ), getSixUrl().get( 11 )
          , getSixUrl().get( 12 ), getSixUrl().get( 13 ), getSixUrl().get( 14 ),
          getSixUrl().get( 15 )
          , getSixUrl().get( 16 ), getSixUrl().get( 17 ), getSixUrl().get( 18 ),
          getSixUrl().get( 19 )
          , getSixUrl().get( 20 ), getSixUrl().get( 21 ), getSixUrl().get( 22 )
      };

      private static ArrayList<String> getOneUrl ( ) {

            if( oneList == null ) {
                  synchronized(ArrayList.class) {
                        if( oneList == null ) {
                              oneList = new ArrayList<>();
                              for( int i = 1; i < 13; i++ ) {
                                    oneList.add(
                                        "http://ojyz0c8un.bkt.clouddn.com/home_one_" + i + ".png" );
                              }
                              return oneList;
                        }
                  }
            }
            return oneList;
      }

      private static ArrayList<String> getSixUrl ( ) {

            if( sixList == null ) {
                  synchronized(ArrayList.class) {
                        if( sixList == null ) {
                              sixList = new ArrayList<>();
                              for( int i = 1; i < 24; i++ ) {
                                    sixList.add(
                                        "http://ojyz0c8un.bkt.clouddn.com/home_six_" + i + ".png" );
                              }
                              return sixList;
                        }
                  }
            }
            return sixList;
      }
}
