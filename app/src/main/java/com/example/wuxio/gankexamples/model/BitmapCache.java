package com.example.wuxio.gankexamples.model;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.bean.BeautiesBean;
import com.example.wuxio.gankexamples.root.OnAppExitManager;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-10-14:10:52
 */
public class BitmapCache {

      private static final String TAG = BitmapCache.class.getSimpleName();

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
      public static void downLoadPicture ( String url ) {

            boolean contains = sBitmapLoader.containsOfFile( url );

            if( !contains ) {
                  sBitmapLoader.download( url );
                  File file = sBitmapLoader.getFile( url );
                  Log.e( TAG, "downLoadPicture : 缓存图片完成 :" + url + " " + file );
            }
      }

      public static boolean hasPictureCache ( String url ) {

            return sBitmapLoader.containsOfFile( url );
      }

      /**
       * {@link #loadListBitmaps(BeautiesBean, int, int, OnListBitmapsLoadedListener)}回调
       */
      public interface OnListBitmapsLoadedListener {

            /**
             * 加载完成
             *
             * @param index 起始
             * @param count 数量
             * @param result bitmaps
             */
            void onLoaded ( int index, int count, List<Bitmap> result );
      }

      public static List<Bitmap> loadListBitmaps ( List<String> urls ) {

            /* 1.找出没有缓存的图片 */
            ArrayList<String> notCacheUrl = new ArrayList<>();
            for( String url : urls ) {
                  if( !sBitmapLoader.containsOfFile( url ) ) {
                        notCacheUrl.add( url );
                  }
            }

            int size = notCacheUrl.size();
            if( size > 0 ) {

                  ArrayList<DownLoadBitmapRunnable> runnableList = new ArrayList<>( size );
                  for( String s : notCacheUrl ) {
                        Log.e( TAG, "loadListBitmaps : 加载一组图片:没有缓存的图片 " + s );
                        runnableList.add( new DownLoadBitmapRunnable( s ) );
                  }
                  PoolExecutor.execute( runnableList );
            }

            /* 3.加载成bitmap */
            int urlSize = urls.size();
            int width = ScreenSize.getWidth();
            int height = ScreenSize.getHeight();
            ArrayList<Bitmap> result = new ArrayList<>( urlSize );
            for( String url : urls ) {
                  Bitmap bitmap = sBitmapLoader.load( url, width, height );
                  result.add( bitmap );
            }

            Log.e( TAG, "loadListBitmaps : 加载一组图片完成" );
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
