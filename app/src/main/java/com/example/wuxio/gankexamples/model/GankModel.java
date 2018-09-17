package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import com.example.wuxio.gankexamples.constant.Constant;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.GankDay;
import com.example.wuxio.gankexamples.model.bean.GankHistory;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.MainExecutor;
import com.threekilogram.objectbus.executor.PoolExecutor;
import com.threekilogram.objectbus.tools.Blocker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import tech.threekilogram.depository.cache.bitmap.BitmapConverter.ScaleMode;
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
       * 图片加载
       */
      private static BitmapLoader sBitmapLoader;
      /**
       * 所有发布过文章的历史日期bean
       */
      private static GankHistory  sGankHistory;
      /**
       * 用于{@link #sGankHistory}为null时,制造屏障
       */
      private static Blocker sHistoryLoadFinishedBlocker = new Blocker();
      /**
       * 加载历史记录bean
       */
      private static JsonLoader<GankHistory> sHistoryLoader;
      /**
       * 加载日期gank bean
       */
      private static JsonLoader<GankDay>     sDayLoader;

      /**
       * 初始化
       *
       * @param context context
       */
      public static void init ( Context context ) {

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

            initHistory( context );
      }

      /**
       * 初始化history
       */
      private static void initHistory ( Context context ) {

            PoolExecutor.execute( ( ) -> {

                  String url = GankUrl.historyUrl();

                  /* 先从网络获取最新的数据 */
                  sGankHistory = sHistoryLoader.loadFromNet( url );
                  if( sGankHistory == null ) {

                        /* 网络获取失败,从本地缓存读取 */
                        sGankHistory = sHistoryLoader.loadFromFile( url );
                        if( sGankHistory == null ) {
                              Log.e( TAG, "initHistory : 没有缓存文件" );
                              sGankHistory = new GankHistory();
                        }
                  } else {
                        /* 缓存网络最新的历史日期数据 */
                        cacheHistoryBean( url );
                  }

                  /* 通知所有等待sGankHistory的线程加载好了 */
                  sHistoryLoadFinishedBlocker.resumeAll();
                  /* 缓存day数据 */
                  cacheHistoryCategoryBean();

                  /* 如果历史记录从所有方式都读取失败了,检查网络,如果网络没有问题,那么是服务器挂了 */
                  if( sGankHistory.getResults() == null ) {

                        int currentNetState = NetStateChangeManager.getInstance()
                                                                   .getCurrentNetState();

                        if( currentNetState == NetStateValue.WIFI_MOBILE_DISCONNECT ) {
                              Log.e( TAG, "run : 没有网络" );
                              MainExecutor.execute( ( ) -> Toast.makeText(
                                  context,
                                  "没有网络",
                                  Toast.LENGTH_SHORT
                              ).show() );
                        } else {
                              Log.e( TAG, "run : 获取数据失败" );
                              MainExecutor.execute( ( ) -> Toast.makeText(
                                  context,
                                  "没有网络",
                                  Toast.LENGTH_SHORT
                              ).show() );
                        }
                  }
            } );
      }

      /**
       * 后台缓存最新的history
       *
       * @param url mUrl
       */
      private static void cacheHistoryBean ( String url ) {

            PoolExecutor.execute( ( ) -> sHistoryLoader.saveToFile( url, sGankHistory ) );
      }

      /**
       * 根据history缓存本地没有的{@link GankDay}记录
       */
      private static void cacheHistoryCategoryBean ( ) {

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
       * @param url mUrl
       * @param width bitmap宽度
       * @param height bitmap高度
       * @param listener 回调监听
       */
      public static void loadBitmap (
          String url,
          int width,
          int height,
          OnLoadBitmapFinishedListener listener ) {

            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {
                  /* 如果已经有该url对应bitmap缓存,那么读取缓存 */
                  if( sBitmapLoader.containsOf( url ) ) {

                        Bitmap bitmap = sBitmapLoader.loadFromMemory( url );
                        if( bitmap == null ) {
                              sBitmapLoader.configBitmap( width, height );
                              bitmap = sBitmapLoader.loadFromFile( url );
                        }
                        bus.setResult( url, bitmap );
                  } else {

                        /* 没有该url对应bitmap缓存,从网络读取 */
                        sBitmapLoader.configBitmap( width, height );
                        sBitmapLoader.download( url );
                  }
            } ).toMain( ( ) -> {

                  /* 回到主线程回调监听 */
                  Bitmap result = bus.getResultOff( url );
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

      /**
       * 加载分类数据beauty,并在主线程回调结果
       *
       * @param index which
       * @param listener 回调
       */
      public static void loadBeautyItem ( int index, OnLoadCategoryItemFinishedListener listener ) {

            String key = Constant.BEAUTY + index;

            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {

                  if( sGankHistory == null ) {
                        sHistoryLoadFinishedBlocker.pause( 30000 );
                  }

                  List<String> results = sGankHistory.getResults();
                  if( results != null ) {

                        /* 从day bean 中读取最新的bitmap mUrl */
                        String date = results.get( index );
                        String dayUrl = GankUrl.dayUrl( date );

                        /* 县从本地缓存读取数据,如果没有从网络读取 */
                        GankDay gankDay = sDayLoader.load( dayUrl );
                        if( gankDay == null ) {
                              gankDay = sDayLoader.loadFromNet( dayUrl );
                        }

                        if( gankDay != null ) {

                              GankCategoryItem gankCategoryItem = gankDay.getResults().get福利()
                                                                         .get( 0 );
                              bus.setResult( key, gankCategoryItem );
                        }
                  }
            } ).toMain( ( ) -> {

                  GankCategoryItem result = bus.getResultOff( key );
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
       * @param url mUrl
       * @param listener 回调
       */
      public static void downLoadImage ( String url, OnDownLoadFinishedListener listener ) {

            PoolExecutor.execute( ( ) -> {

                  sBitmapLoader.download( url );
                  File file = sBitmapLoader.getFile( url );

                  if( listener != null ) {
                        listener.onFinished( url, file );
                  }
            } );
      }

      /**
       * 下载完成后的回调
       */
      public interface OnDownLoadFinishedListener {

            /**
             * 下载完成后回调
             *
             * @param url mUrl
             * @param file 该url对应文件
             */
            void onFinished ( String url, File file );
      }

      /**
       * 加载一组bitmap对象
       *
       * @param startIndex 起始索引
       * @param count 数据总数
       */
      public static void loadListBitmaps (
          int startIndex, int count,
          int width,
          int height,
          @ScaleMode int scaleMode,
          OnLoadListFinishedListener<Bitmap> listener ) {

            String key = GankUrl.BEAUTY + "/" + startIndex + "/" + count;
            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {

                  if( sGankHistory == null ) {
                        sHistoryLoadFinishedBlocker.pause( 10000 );
                  }
                  if( sGankHistory == null ) {
                        return;
                  }
                  List<String> results = sGankHistory.getResults();
                  if( results == null ) {
                        return;
                  }

                  int end = startIndex + count;
                  List<String> urls = new ArrayList<>();

                  flag:
                  for( int i = startIndex; i < end; i++ ) {
                        String date = results.get( i );
                        String dayUrl = GankUrl.dayUrl( date );
                        GankDay gankDay = sDayLoader.load( dayUrl );
                        if( gankDay == null ) {
                              gankDay = sDayLoader.loadFromNet( dayUrl );
                        }
                        List<GankCategoryItem> beautyItems = gankDay.getResults().get福利();
                        for( GankCategoryItem beautyItem : beautyItems ) {
                              String url = beautyItem.getUrl();
                              urls.add( url );
                              if( urls.size() >= count ) {
                                    break flag;
                              }
                        }
                  }

                  List<LoadBitmapCallable> callables = new ArrayList<>();
                  for( String url : urls ) {
                        LoadBitmapCallable callable = new LoadBitmapCallable(
                            url,
                            width,
                            height,
                            scaleMode
                        );
                        callables.add( callable );
                  }
                  List<Bitmap> bitmaps = PoolExecutor.submitAndGet( callables );
                  bus.setResult( key, bitmaps );
            } ).toMain( ( ) -> {

                  List<Bitmap> result = bus.getResultOff( key );
                  listener.onLoadFinished( result );
            } ).run();
      }

      /**
       * 异步执行加载图片
       */
      private static class LoadBitmapCallable implements Callable<Bitmap> {

            private String mUrl;
            private int    mWidth;
            private int    mHeight;
            private int    mScaleMode;

            LoadBitmapCallable ( String url, int width, int height, int scaleMode ) {

                  mUrl = url;
                  mWidth = width;
                  mHeight = height;
                  mScaleMode = scaleMode;
            }

            @Override
            public Bitmap call ( ) throws Exception {

                  sBitmapLoader.configBitmap( mWidth, mHeight, mScaleMode );
                  Bitmap bitmap = sBitmapLoader.load( mUrl );
                  if( bitmap == null ) {
                        bitmap = sBitmapLoader.loadFromNet( mUrl );
                  }
                  return bitmap;
            }
      }

      /**
       * 用于加载完成一组数据的回调
       */
      public interface OnLoadListFinishedListener<V> {

            /**
             * 加载一组数据完成的回调
             *
             * @param result 结果
             */
            void onLoadFinished ( List<V> result );
      }
}
