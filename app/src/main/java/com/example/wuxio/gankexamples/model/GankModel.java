package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import tech.threekilogram.depository.bitmap.BitmapLoader;
import tech.threekilogram.depository.json.GsonConverter;
import tech.threekilogram.depository.json.JsonLoader;
import tech.threekilogram.depository.net.LoadingUrls;
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
      private static ObjectBus                sObjectBus;
      /**
       * 图片加载
       */
      private static BitmapLoader             sBitmapLoader;
      /**
       * json加载
       */
      private static JsonLoader<GankCategory> sJsonLoader;
      /**
       * 配置文件
       */
      private static Preference               sPreference;
      /**
       * 防止重复加载
       */
      private static LoadingUrls              sUrls;
      /**
       * {@link com.example.wuxio.gankexamples.splash.SplashActivity#mLogoImage}图片地址
       */
      private static final String TODAY_IMAGE_URL = "today_image_url";

      /**
       * 初始化
       *
       * @param context context
       */
      public static void init ( Context context ) {

            sObjectBus = ObjectBus.newList();

            sBitmapLoader = new BitmapLoader(
                (int) Runtime.getRuntime().maxMemory() >> 3,
                context.getExternalFilesDir( "gankImage" )
            );

            sJsonLoader = new JsonLoader<>(
                context.getExternalFilesDir( "gankBean" ),
                new GsonConverter<>( GankCategory.class )
            );

            sPreference = new Preference( context, "gankConfig" );

            sUrls = new LoadingUrls();
      }

      /**
       * 为{@link com.example.wuxio.gankexamples.splash.SplashActivity}设置展示图片
       */
      public static void prepareSplashImage ( ImageView view, int width, int height ) {

            loadNewTodayImage();

            String imageUrl = sPreference.getString( TODAY_IMAGE_URL );
            if( imageUrl != null ) {

                  Bitmap bitmap = sBitmapLoader.loadFromMemory( imageUrl );
                  if( bitmap == null ) {

                        /* 2.内存中没有,尝试从文件读取 */
                        if( !sBitmapLoader.containsOfFile( imageUrl ) ) {
                              return;
                        }

                        sBitmapLoader.configBitmap( width, height );
                        bitmap = sBitmapLoader.loadFromFile( imageUrl );
                        if( bitmap == null ) {
                              return;
                        }
                        view.setImageBitmap( bitmap );
                  } else {

                        /* 1.先尝试从内存中读取是否已经有该宽度高度bitmap */
                        if( bitmap.getWidth() >= width || bitmap.getHeight() >= height ) {
                              view.setImageBitmap( bitmap );
                        } else {
                              sBitmapLoader.configBitmap( width, height );
                              bitmap = sBitmapLoader.loadFromFile( imageUrl );
                              if( bitmap == null ) {
                                    return;
                              }
                              view.setImageBitmap( bitmap );
                        }
                  }
            }
      }

      /**
       * 尝试加载是否有新的图片
       */
      private static void loadNewTodayImage ( ) {

            int currentNetState = NetStateChangeManager.getInstance().getCurrentNetState();
            /* 处于 wifi 状态 */
            if( currentNetState > NetStateValue.ONLY_MOBILE_CONNECT ) {
                  PoolExecutor.execute( ( ) -> {

                        /* 1.从网络加载jsonBean */
                        String url = GankUrl.splashImageUrl();
                        if( sUrls.isLoading( url ) ) {
                              return;
                        }
                        GankCategory gankCategory = sJsonLoader.loadFromNet( url );
                        sUrls.removeLoadingUrl( url );
                        if( gankCategory == null ) {
                              return;
                        }
                        /* 2. 读取bean中url */
                        String todayImageUrl = gankCategory.getResults().get( 0 ).getUrl();
                        if( todayImageUrl == null ) {
                              return;
                        }

                        /* 3.如果没有该图片那么下载他,并更新配置 */
                        File file = sBitmapLoader.getFile( todayImageUrl );
                        if( file.exists() ) {
                              return;
                        }
                        if( sUrls.isLoading( todayImageUrl ) ) {
                              return;
                        }
                        sBitmapLoader.downLoad( todayImageUrl );
                        sUrls.removeLoadingUrl( todayImageUrl );
                        file = sBitmapLoader.getFile( todayImageUrl );
                        if( file.exists() ) {
                              sPreference.save( TODAY_IMAGE_URL, todayImageUrl );
                        }
                  } );
            }
      }
}
