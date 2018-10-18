package com.example.wuxio.gankexamples.file;

import com.example.wuxio.gankexamples.App;
import java.io.File;

/**
 * @author wuxio 2018-05-05:19:13
 */
public class FileManager {

      private static final String GANK    = "gank";
      private static final String PICTURE = "picture";

      private static File sAppFile;

      public static void init ( ) {

            App app = App.INSTANCE;
            File dir = app.getExternalFilesDir( GANK );
            if( dir == null ) {
                  File filesDir = app.getFilesDir();
                  dir = new File( filesDir, GANK );
            }

            if( !dir.exists() ) {
                  boolean mkdirs = dir.mkdirs();
            }

            sAppFile = dir;
      }

      /**
       * @return app 目录,位于外存储或者内存储的gank目录下
       */
      public static File getAppFile ( ) {

            return sAppFile;
      }

      /**
       * @return 保存图片的文件夹
       */
      public static File getPictureFile ( ) {

            File file = new File( getAppFile(), PICTURE );
            if( !file.exists() ) {
                  boolean mkdirs = file.mkdirs();
            }
            return file;
      }

      /**
       * @return 保存分类beauty的文件夹
       */
      public static File getFileByType ( String type ) {

            File file = new File( getAppFile(), type );
            if( !file.exists() ) {
                  boolean mkdirs = file.mkdirs();
            }
            return file;
      }
}
