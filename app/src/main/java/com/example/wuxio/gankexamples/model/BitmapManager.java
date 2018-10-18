package com.example.wuxio.gankexamples.model;

import android.graphics.Bitmap;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.log.AppLog;
import com.example.wuxio.gankexamples.root.OnAppExitManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.model.cache.bitmap.BitmapLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-10-14:10:52
 */
public class BitmapManager {

      /**
       * 加载图片
       */
      private static BitmapLoader sBitmapLoader;

      /**
       * 初始化变量
       */
      public static void init ( ) {

            if( sBitmapLoader == null ) {
                  File pictureFile = FileManager.getPictureFile();
                  int memorySize = (int) Runtime.getRuntime().maxMemory() >> 3;
                  sBitmapLoader = new BitmapLoader(
                      memorySize,
                      pictureFile
                  );
            }

            OnAppExitManager.addListener( ( ) -> sBitmapLoader.clearMemory() );
      }

      /**
       * 下载url对应的图片
       */
      public static File downLoadPicture ( String url ) {

            boolean contains = sBitmapLoader.containsOfFile( url );

            if( !contains ) {
                  sBitmapLoader.download( url );
            }

            return sBitmapLoader.getFile( url );
      }

      public static File getFile ( String url ) {

            return sBitmapLoader.getFile( url );
      }

      public static boolean hasPictureCache ( String url ) {

            return sBitmapLoader.containsOfFile( url );
      }

      public static List<Bitmap> loadListBitmaps ( List<String> urls ) {

            /* 找出没有缓存的图片 */
            ArrayList<String> notCacheUrl = new ArrayList<>();
            for( String url : urls ) {
                  if( !sBitmapLoader.containsOfFile( url ) ) {
                        notCacheUrl.add( url );
                  }
            }

            /* 下载图片 */
            int size = notCacheUrl.size();
            if( size > 0 ) {

                  List<Runnable> runnableList = new ArrayList<>( size );
                  for( String s : notCacheUrl ) {
                        runnableList.add( new DownLoadBitmapRunnable( s ) );
                  }
                  PoolExecutor.execute( runnableList );
            }

            /* 加载成bitmap */
            int urlSize = urls.size();
            int width = ScreenSize.getWidth();
            int height = ScreenSize.getHeight();
            ArrayList<Bitmap> result = new ArrayList<>( urlSize );
            for( String url : urls ) {
                  Bitmap bitmap = sBitmapLoader.load( url, width, height );
                  result.add( bitmap );
                  if( bitmap == null ) {
                        AppLog.addLog( "下载图片失败 " + url );
                  }
            }

            return result;
      }

      public static Bitmap loadBitmap (
          String url, int width, int height ) {

            return sBitmapLoader.load( url, width, height );
      }

      public static Bitmap loadBitmapFromMemory ( String url ) {

            return sBitmapLoader.loadFromMemory( url );
      }

      public static Bitmap loadBitmapFromFile ( String url, int width, int height ) {

            return sBitmapLoader.loadFromFile( url, width, height );
      }

      /**
       * 下载图片的runnable
       */
      private static class DownLoadBitmapRunnable implements Runnable {

            private String mUrl;

            public DownLoadBitmapRunnable ( String url ) {

                  this.mUrl = url;
            }

            @Override
            public void run ( ) {

                  sBitmapLoader.download( mUrl );
            }
      }
}
