package com.example.wuxio.gankexamples.file;

import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.GankUrl;
import java.io.File;
import tech.threekilogram.depository.function.encode.StringHash;

/**
 * @author wuxio 2018-05-05:19:13
 */
public class FileManager {

      private static File sAppFile;
      private static File sBeanStringFile;
      private static File sPictureFile;
      private static File sBeautyFile;
      private static File sBeautiesBeanFile;
      private static File sBeautyJsonFile;
      private static File sLatestBeautyJsonFile;

      public static final String GANK        = "gank";
      public static final String BEAN_STRING = "beanString";
      public static final String PICTURE     = "picture";
      public static final String BEAUTY      = "beauty";

      public static final String BEAUTY_ALL_URL_HASH = StringHash.hash( GankUrl.beautyAllUrl() );

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

      /**
       * @return 保存分类beauty的文件夹
       */
      public static File getBeautyFile ( ) {

            if( sBeautyFile == null ) {
                  sBeautyFile = new File( sAppFile, BEAUTY );
                  if( !sBeautyFile.exists() ) {
                        sBeautyFile.mkdirs();
                  }
            }
            return sBeautyFile;
      }

      /**
       * @return 保存分类beauty的文件夹
       */
      public static File getBeautiesBeanFile ( ) {

            if( sBeautiesBeanFile == null ) {
                  File beautyFile = getBeautyFile();
                  sBeautiesBeanFile = new File( beautyFile, BEAUTY_ALL_URL_HASH );
            }
            return sBeautiesBeanFile;
      }

      /**
       * @return 保存分类beauty的文件夹
       */
      public static File getBeautyJsonFile ( ) {

            if( sBeautyJsonFile == null ) {
                  File beanStringFile = getBeanStringFile();
                  sBeautyJsonFile = new File( beanStringFile, BEAUTY_ALL_URL_HASH + ".json" );
            }
            return sBeautyJsonFile;
      }

      public static File getLatestBeautyJsonFile ( ) {

            if( sLatestBeautyJsonFile == null ) {
                  File beanStringFile = getBeanStringFile();
                  sLatestBeautyJsonFile = new File( beanStringFile, "beautyLatest.json" );
            }
            return sLatestBeautyJsonFile;
      }
}
