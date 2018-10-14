package com.example.wuxio.gankexamples.splash;

import android.util.Log;
import android.widget.Toast;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.executor.MainExecutor;
import java.lang.ref.WeakReference;
import tech.threekilogram.depository.preference.PreferenceLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-09-11:9:08
 */
public class SplashModel {

      private static final String TAG = SplashModel.class.getSimpleName();

      /**
       * splash preference name
       */
      private static final String SPLASH_CONFIG           = "splash_config";
      /**
       * splash image logo url key
       */
      private static final String SPLASH_IMAGE_CACHED_URL = "splash_image_cached_url";

      /**
       * 读取的image url
       */
      private static String                        sSplashImageUrl;
      /**
       * splash 配置
       */
      private static PreferenceLoader              sPreferenceLoader;
      /**
       * {@link SplashActivity}
       */
      private static WeakReference<SplashActivity> sRef;

      static void bind ( SplashActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      /**
       * 为{@link SplashActivity#mLogoImage}设置图片
       */
      static void setSplashImage ( ) {

            /* 读取配置的splash图片对应url */
            if( sPreferenceLoader == null ) {
                  sPreferenceLoader = new PreferenceLoader(
                      App.INSTANCE,
                      SPLASH_CONFIG
                  );
            }

            sSplashImageUrl = sPreferenceLoader.getString( SPLASH_IMAGE_CACHED_URL );

            Log.e( TAG, "setSplashImage : 配置的splash图片地址: " + sSplashImageUrl );

            if( sSplashImageUrl != null ) {
                  if( BitmapCache.hasPictureCache( sSplashImageUrl ) ) {

                        /* 设置图片 */
                        int width = ScreenSize.getWidth();
                        int height = ScreenSize.getHeight();
                        BitmapCache.loadBitmap(
                            sSplashImageUrl,
                            width,
                            height,
                            ( url, bitmap ) -> {
                                  try {
                                        sRef.get().mLogoImage.setImageBitmap( bitmap );
                                        Log.e( TAG, "setSplashImage : 设置splash图片成功" );
                                  } catch(Exception e) {
                                        /* nothing to worry */
                                  }
                            }
                        );
                  } else {
                        Log.e( TAG, "setSplashImage : splash图片缓存失效,重新下载" );
                        BitmapCache.downLoadPicture( sSplashImageUrl );
                  }
            }

            /* 更新一下splashUrl配置,用于下次操作时读取 */
            updateSplashImageUrlForNextTime();
      }

      /**
       * 更新新的 splash 图片
       */
      private static void updateSplashImageUrlForNextTime ( ) {

            BeanLoader.loadLatestBeautyUrl(
                url -> {
                      if( url != null ) {
                            sPreferenceLoader.save( SPLASH_IMAGE_CACHED_URL, url );
                            BitmapCache.downLoadPicture( url );
                      } else {
                            notifySplashImageUrlForNextTimeIsNull();
                      }
                }
            );
      }

      /**
       * 没有获取到url的处理
       */
      private static void notifySplashImageUrlForNextTimeIsNull ( ) {

            MainExecutor.execute( ( ) -> {

                  if( !NetWork.hasNetwork() ) {
                        Toast.makeText( App.INSTANCE, "没有网络", Toast.LENGTH_SHORT ).show();
                  } else {
                        Toast.makeText( App.INSTANCE, "无法获取数据", Toast.LENGTH_SHORT ).show();
                  }
            } );
      }
}
