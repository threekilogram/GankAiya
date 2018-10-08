package com.example.wuxio.gankexamples.model;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.depository.stream.StreamLoader;

/**
 * @author Liujin 2018-10-07:19:24
 */
public class BeanLoader {

      private static final String TAG = BeanLoader.class.getSimpleName();

      private static BitmapLoader sBitmapLoader;

      public static void init ( ) {

            if( sBitmapLoader == null ) {
                  File pictureFile = FileManager.getPictureFile();
                  int memorySize = (int) Runtime.getRuntime().maxMemory() >> 3;
                  sBitmapLoader = new BitmapLoader(
                      memorySize,
                      pictureFile
                  );
            }
      }

      /**
       * 用于和数据层通信,加载完新的splash回调
       */
      public interface OnLatestBeautyLoadedListener {

            /**
             * 加载完成
             *
             * @param url new url
             */
            void onLoaded ( String url );
      }

      /**
       * 获取最新的beauty分类的最新的一条数据
       */
      public static void loadLatestBeautyUrl ( OnLatestBeautyLoadedListener listener ) {

            PoolExecutor.execute( ( ) -> {

                  /* 没有网络 */
                  if( !NetWork.hasNetwork() ) {
                        listener.onLoaded( null );
                        return;
                  }

                  /* 从网络加载最新的数据 */
                  String latestUrl = GankUrl.beautyLatestUrl();
                  GankCategory gankCategory = StreamLoader
                      .loadJsonFromNet( latestUrl, GankCategory.class );

                  Log.e( TAG, "loadLatestBeautyUrl : 获取到的最新beauty: " + gankCategory );
                  if( gankCategory != null ) {
                        String url = gankCategory.getResults().get( 0 ).getUrl();
                        listener.onLoaded( url );
                  } else {
                        listener.onLoaded( null );
                  }
            } );
      }

      /**
       * 下载url对应的图片
       */
      public static void downLoadPicture ( String url ) {

            boolean contains = sBitmapLoader.containsOfFile( url );
            Log.e( TAG, "downLoadPicture : 是否有图片缓存: " + url + " : " + contains );
            if( !contains ) {
                  PoolExecutor.execute( ( ) -> {

                        sBitmapLoader.download( url );
                        File file = sBitmapLoader.getFile( url );
                        Log.e( TAG, "downLoadPicture : 下载图片完成 :" + url + " " + file );
                  } );
            }
      }

      /**
       * 加载图片完成的回调
       */
      public interface OnBitmapLoadedListener {

            /**
             * 加载完成
             *
             * @param url url
             * @param bitmap url对应图片,可能为null
             */
            void onLoaded ( String url, Bitmap bitmap );
      }

      /**
       * 测试是否有url对应的图片的缓存
       *
       * @param url url
       *
       * @return true : 由对应的缓存
       */
      public static boolean hasPictureCache ( String url ) {

            return sBitmapLoader.containsOfFile( url );
      }

      public static void loadBitmap (
          String url, int width, int height, OnBitmapLoadedListener listener ) {

            Bitmap bitmap = sBitmapLoader.loadFromMemory( url );
            if( bitmap != null ) {
                  listener.onLoaded( url, bitmap );
                  return;
            }

            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {

                  if( !sBitmapLoader.containsOfFile( url ) ) {
                        sBitmapLoader.download( url );
                  }

                  Bitmap result = sBitmapLoader.loadFromFile( url, width, height );
                  bus.setResult( url, result );
            } ).toMain( ( ) -> {

                  if( listener != null ) {
                        Bitmap bitmap1 = bus.getResultOff( url );
                        listener.onLoaded( url, bitmap1 );
                  }
            } ).run();
      }
}
