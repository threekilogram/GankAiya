package com.example.wuxio.gankexamples.file;

import com.example.wuxio.gankexamples.App;
import java.io.File;

/**
 * @author wuxio 2018-05-05:19:13
 */
public class FileManager {

      private static File sAppFile;
      private static File sBeanStringFile;
      private static File sPictureFile;

      public static final String GANK        = "gank";
      public static final String BEAN_STRING = "beanString";
      public static final String PICTURE     = "picture";

      public static void init ( ) {

            App app = App.INSTANCE;
            File dir = app.getExternalFilesDir( GANK );
            if( dir == null ) {
                  File filesDir = app.getFilesDir();
                  dir = new File( filesDir, GANK );
            }

            if( !dir.exists() ) {
                  dir.mkdirs();
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
       * @return 缓存从网络获取的历史bean的目录
       */
      public static File getBeanStringFile ( ) {

            if( sBeanStringFile == null ) {
                  sBeanStringFile = new File( sAppFile, BEAN_STRING );
                  if( !sBeanStringFile.exists() ) {
                        sBeanStringFile.mkdirs();
                  }
            }

            return sBeanStringFile;
      }

      /**
       * @return 保存图片的文件夹
       */
      public static File getPictureFile ( ) {

            if( sPictureFile == null ) {
                  sPictureFile = new File( sAppFile, PICTURE );
                  if( !sPictureFile.exists() ) {
                        sPictureFile.mkdirs();
                  }
            }
            return sPictureFile;
      }
}
