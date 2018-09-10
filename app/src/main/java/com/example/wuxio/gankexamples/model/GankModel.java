package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.depository.cache.json.JsonLoader;
import tech.threekilogram.depository.cache.json.ObjectLoader;
import tech.threekilogram.depository.preference.Preference;
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
       * 图片链接
       */
      private static List<String> sImageUrls = new ArrayList<>();

      /**
       * splash配置
       */
      private static Preference sPreference;
      /**
       * {@link #sPreference}配置名字,app一些配置项
       */
      private static final String GANK_CONFIG = "GankConfig";
      /**
       * 该键对应的值是{@link com.example.wuxio.gankexamples.splash.SplashActivity#mLogoImage}图片地址,
       * 可以直接从本地加载,从本地加载可以防止网络反应慢,设置不及时,体验不好
       */
      private static final String SPLASH_URL  = "SplashUrl";

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

            if( sPreference == null ) {
                  sPreference = new Preference( context, GANK_CONFIG );
            }

            if( sHistoryLoader == null ) {
                  File dir = context.getExternalFilesDir( "gankHistory" );
                  sHistoryLoader = new JsonLoader<>( dir, GankHistory.class );
            }

            if( sDayLoader == null ) {
                  File dir = context.getExternalFilesDir( "ganDay" );
                  sDayLoader = new JsonLoader<>( dir, GankDay.class );
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
       * 为{@link com.example.wuxio.gankexamples.splash.SplashActivity}设置图片,并且准备下次需要的图片
       *
       * @param imageView imageView
       * @param width bitmap宽度
       * @param height bitmap高度
       */
      public static void setSplashBitmap (
          ImageView imageView,
          int width,
          int height ) {

            WeakReference<ImageView> ref = new WeakReference<>( imageView );

            /* 从本地加载配置图片,并设置给imageView,如果没有配置什么都不做 */
            final String keyBitmap = "splash_bitmap";
            sObjectBus.toPool( ( ) -> {

                  String url = sPreference.getString( SPLASH_URL );
                  if( url != null ) {
                        Bitmap bitmap = sBitmapLoader.loadFromMemory( url );
                        if( bitmap == null ) {
                              sBitmapLoader.configBitmap( width, height );
                              bitmap = sBitmapLoader.loadFromFile( url );
                        }
                        if( bitmap != null ) {

                              sObjectBus.setResult( keyBitmap, bitmap );
                        }
                  }
            } ).toMain( ( ) -> {

                  Bitmap result = sObjectBus.getResultOff( keyBitmap );
                  if( result != null ) {
                        ImageView view = ref.get();
                        if( view != null ) {
                              view.setImageBitmap( result );
                        }
                  }
            } ).run();

            updateSplashPreference();
      }

      private static void updateSplashPreference ( ) {

            /* 更新本地配置图片,用于下次加载时使用 */
            PoolExecutor.execute( ( ) -> {

                  if( sGankHistory != null ) {

                        /* 从day bean 中读取最新的bitmap url */
                        String date = sGankHistory.getResults().get( 0 );
                        String dayUrl = GankUrl.dayUrl( date );
                        GankDay gankDay = sDayLoader.load( dayUrl );
                        if( gankDay == null ) {
                              return;
                        }
                        String url = gankDay.getResults().get福利().get( 0 ).getUrl();
                        if( url == null ) {
                              return;
                        }

                        /* 如果本地没有该图片,缓存图片 */
                        if( !sBitmapLoader.containsOfFile( url ) ) {
                              sBitmapLoader.download( url );
                        }

                        /* 更新配置 */
                        if( sBitmapLoader.containsOfFile( url ) ) {
                              sPreference.save( SPLASH_URL, url );
                        }
                  } else {

                        String beautyUrl = GankUrl.splashImageUrl();
                        GankCategory category = ObjectLoader
                            .loadFromNet( beautyUrl, GankCategory.class );
                        String url = category.getResults().get( 0 ).getUrl();

                        /* 如果本地没有该图片,缓存图片 */
                        if( !sBitmapLoader.containsOfFile( url ) ) {
                              sBitmapLoader.download( url );
                        }

                        /* 更新配置 */
                        if( sBitmapLoader.containsOfFile( url ) ) {
                              sPreference.save( SPLASH_URL, url );
                        }
                  }
            } );
      }

      /**
       * 设置mainActivity banner 图片
       */
      public static void setBannerBitmaps ( MainActivity activity, int width, int height ) {

            PoolExecutor.execute( new Runnable() {

                  @Override
                  public void run ( ) {

                  }
            } );
      }

      public static void main ( String[] args ) {

      }
}
