package com.example.wuxio.gankexamples.utils;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liujin 2018-10-08:15:08
 */
public class DateUtil {

      public static Date getDate ( String date ) {

            date = date.replace( "Z", " UTC" );

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS Z"
            );

            try {
                  return format.parse( date );
            } catch(ParseException e) {
                  e.printStackTrace();
            }

            return null;
      }

      public static String getTodayString ( ) {

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
            return format.format( new Date() ) + "T00:00:00.0Z";
      }

      /**
       * 通过时间秒毫秒数判断两个时间的间隔
       */
      public static int betweenDays ( Date date1, Date date2 ) {

            return (int) ( ( date2.getTime() - date1.getTime() ) / ( 1000 * 3600 * 24 ) );
      }

      public static boolean isLater ( String date1, String date2 ) {

            return getDate( date2 ).getTime() - getDate( date1 ).getTime() > 0;
      }

      /**
       * true data2 晚于 date1
       */
      public static boolean isLater ( Date date1, Date date2 ) {

            return date2.getTime() - date1.getTime() > 0;
      }
}
