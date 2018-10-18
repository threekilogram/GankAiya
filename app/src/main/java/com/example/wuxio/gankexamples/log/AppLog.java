package com.example.wuxio.gankexamples.log;

import android.support.annotation.NonNull;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import tech.threekilogram.model.cache.json.ObjectLoader;

/**
 * @author Liujin 2018-10-18:12:26
 */
public class AppLog {

      private static final String TAG = "GankAiYa";

      private static boolean  isRecord  = true;
      private static Calendar sCalendar = Calendar.getInstance( Locale.CHINA );

      private static ArrayList<String> sLogs = new ArrayList<>();

      public static void addLog ( String msg ) {

            if( isRecord ) {

                  String log = createLogWithDate( msg + " " );
                  sLogs.add( log );
            }
      }

      public static void saveLog ( ) {

            if( isRecord ) {
                  File appFile = FileManager.getAppFile();
                  File logDir = new File( appFile, "log" );
                  if( !logDir.exists() ) {
                        boolean mkdirs = logDir.mkdirs();
                  }
                  File log = new File( logDir, "log_" + System.currentTimeMillis() + ".json" );
                  LogBean logBean = new LogBean();
                  logBean.setLogs( sLogs );
                  ObjectLoader.toFile( log, logBean, LogBean.class );
                  Log.e( TAG, "saveLog : " + log + " " + log.exists() );
                  sLogs.clear();
            }
      }

      @NonNull
      private static String createLogWithDate ( String msg ) {

            sCalendar.setTime( new Date( System.currentTimeMillis() ) );
            return sCalendar.get( Calendar.YEAR ) + "-"
                + ( sCalendar.get( Calendar.MONTH ) + 1 ) + "-"
                + sCalendar.get( Calendar.DAY_OF_MONTH ) + " "
                + sCalendar.get( Calendar.HOUR_OF_DAY ) + ":"
                + sCalendar.get( Calendar.MINUTE ) + ":"
                + sCalendar.get( Calendar.SECOND ) + msg;
      }

      private static class LogBean {

            private List<String> logs;

            public List<String> getLogs ( ) {

                  return logs;
            }

            public void setLogs ( List<String> logs ) {

                  this.logs = logs;
            }
      }
}
