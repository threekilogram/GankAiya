package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.constant.Constant;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.GankDay;
import com.example.wuxio.gankexamples.model.bean.GankHistory;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.util.List;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.depository.cache.json.JsonLoader;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateValue;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-08-15
 * @time: 17:21
 */
public class GankModel {

      private static final String TAG = GankModel.class.getSimpleName();

      /**
       * 异步任务
       */
      private static ObjectBus               sObjectBus;
      /**
       * 图片加载
       */
      private static BitmapLoader            sBitmapLoader;
      /**
       * 加载发布过的历史日期
       */
      private static JsonLoader<GankHistory> sHistoryLoader;
      private static GankHistory             sGankHistory;
      private static JsonLoader<GankDay>     sDayLoader;

      /**
       * 初始化
       *
       * @param context context
       */
      public static void init ( Context context ) {

            if( sObjectBus == null ) {
                  sObjectBus = ObjectBus.newList();
            }

            if( sBitmapLoader == null ) {
                  sBitmapLoader = new BitmapLoader(
                      (int) Runtime.getRuntime().maxMemory() >> 3,
                      context.getExternalFilesDir( "gankImage" )
                  );
            }

            if( sHistoryLoader == null ) {
                  File dir = context.getExternalFilesDir( "gankHistory" );
                  sHistoryLoader = new JsonLoader<>( -1, dir, GankHistory.class );
            }

            if( sDayLoader == null ) {
                  File dir = context.getExternalFilesDir( "ganDay" );
                  sDayLoader = new JsonLoader<>( 60, dir, GankDay.class );
            }

            initHistory();
      }

      /**
       * 初始化history
       */
      private static void initHistory ( ) {

            if( sGankHistory != null ) {
                  cacheHistory();
                  return;
            }

            PoolExecutor.execute( ( ) -> {

                  String url = GankUrl.historyUrl();
                  /* 先从网络获取最新的数据 */
                  sHistoryLoader.removeFromFile( url );
                  sGankHistory = sHistoryLoader.loadFromDownload( url );

                  /* 如果网络获取失败那么从缓存读取 */
                  if( sGankHistory == null ) {
                        sGankHistory = sHistoryLoader.loadFromFile( url );
                  }

                  /* 缓存读取失败了,检查网络,如果网络没有问题,那么是服务器挂了 */
                  if( sGankHistory == null ) {

                        int currentNetState = NetStateChangeManager.getInstance()
                                                                   .getCurrentNetState();

                        if( currentNetState == NetStateValue.WIFI_MOBILE_DISCONNECT ) {
                              Log.e( TAG, "run : 没有网络" );
                        } else {
                              Log.e( TAG, "run : 服务器挂了" );
                        }
                  } else {
                        cacheHistory();
                  }
            } );
      }

      /**
       * 缓存history
       */
      private static void cacheHistory ( ) {

            /* 没有历史数据 */
            if( sGankHistory == null ) {
                  return;
            }

            /* 就绪,开始缓存 */
            PoolExecutor.execute( ( ) -> {

                  /* 判断是否处于wifi状态 */
                  NetStateChangeManager netState = NetStateChangeManager.getInstance();
                  /* 所有日期 */
                  List<String> results = sGankHistory.getResults();
                  for( String result : results ) {

                        String url = GankUrl.dayUrl( result );
                        if( !sDayLoader.containsOfFile( url ) ) {

                              /* 缓存时判断网络状态 */
                              if( netState.getCurrentNetState()
                                  <= NetStateValue.ONLY_MOBILE_CONNECT ) {

                                    /* app 退出时会停止,因为解注册了网络监听 */
                                    return;
                              }

                              /* wifi状态,缓存数据 */
                              sDayLoader.download( url );
                        }
                  }
            } );
      }

      /**
       * 加载指定url对应的图片,并且按照配置读取,读取完毕后通知回调
       *
       * @param url url
       * @param width bitmap宽度
       * @param height bitmap高度
       * @param listener 回调监听
       */
      public static void loadBitmap (
          String url,
          int width,
          int height,
          OnLoadBitmapFinishedListener listener ) {

            sObjectBus.toPool( ( ) -> {
                  /* 如果已经有该url对应bitmap缓存,那么读取缓存 */
                  if( sBitmapLoader.containsOf( url ) ) {

                        Bitmap bitmap = sBitmapLoader.loadFromMemory( url );
                        if( bitmap == null ) {
                              sBitmapLoader.configBitmap( width, height );
                              bitmap = sBitmapLoader.loadFromFile( url );
                        }
                        sObjectBus.setResult( url, bitmap );
                  } else {

                        /* 没有该url对应bitmap缓存,从网络读取 */
                        sBitmapLoader.configBitmap( width, height );
                        sBitmapLoader.download( url );
                  }
            } ).toMain( ( ) -> {

                  /* 回到主线程回调监听 */
                  Bitmap result = sObjectBus.getResultOff( url );
                  listener.onFinished( url, result );
            } ).run();
      }

      /**
       * {@link #loadBitmap(String, int, int, OnLoadBitmapFinishedListener)}图片加载完成回调
       */
      public interface OnLoadBitmapFinishedListener {

            /**
             * 图片加载完成,回调发生在主线程
             *
             * @param url 请求的url
             * @param bitmap 根据url获取的图片,可能为null
             */
            void onFinished ( String url, Bitmap bitmap );
      }

      public static void loadBeautyItem ( int index, OnLoadCategoryItemFinishedListener listener ) {

            String key = Constant.BEAUTY + index;

            sObjectBus.toPool( ( ) -> {

                  if( sGankHistory != null ) {

                        /* 从day bean 中读取最新的bitmap url */
                        String date = sGankHistory.getResults().get( index );
                        String dayUrl = GankUrl.dayUrl( date );
                        GankDay gankDay = sDayLoader.load( dayUrl );

                        if( gankDay == null ) {
                              gankDay = sDayLoader.loadFromNet( dayUrl );
                        }

                        if( gankDay != null ) {

                              GankCategoryItem gankCategoryItem = gankDay.getResults().get福利()
                                                                         .get( 0 );
                              sObjectBus.setResult( key, gankCategoryItem );
                        }
                  }
            } ).toMain( ( ) -> {

                  GankCategoryItem result = sObjectBus.getResultOff( key );
                  listener.onFinished( Constant.BEAUTY, index, result );
            } ).run();
      }

      /**
       * {@link #loadBeautyItem(int, OnLoadCategoryItemFinishedListener)}加载完成回调
       */
      public interface OnLoadCategoryItemFinishedListener {

            /**
             * 根据分类加载完成所需索引条目{@link GankCategoryItem}的回调
             *
             * @param category 分类
             * @param index 索引
             * @param item 对应条目
             */
            void onFinished ( String category, int index, GankCategoryItem item );
      }

      /**
       * 下载url对应的图片
       *
       * @param url url
       * @param listener 回调
       */
      public static void downLoadImage ( String url, OnDownLoadFinishedListener listener ) {

            PoolExecutor.execute( ( ) -> {

                  File file = sBitmapLoader.getFile( url );
                  if( !file.exists() ) {
                        sBitmapLoader.download( url );
                        file = sBitmapLoader.getFile( url );
                  }

                  listener.onFinished( url, file );
            } );
      }

      /**
       * 下载完成后的回调
       */
      public interface OnDownLoadFinishedListener {

            /**
             * 下载完成后回调
             *
             * @param url url
             * @param file 该url对应文件
             */
            void onFinished ( String url, File file );
      }
}
