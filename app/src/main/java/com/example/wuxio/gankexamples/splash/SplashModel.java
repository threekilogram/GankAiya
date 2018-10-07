package com.example.wuxio.gankexamples.splash;

import android.content.Context;
import android.util.Log;
import java.lang.ref.WeakReference;
import tech.threekilogram.depository.preference.PreferenceLoader;

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
       *
       * @param context context
       * @param bitmapWidth width
       * @param bitmapHeight height
       */
      static void setSplashImage ( Context context, int bitmapWidth, int bitmapHeight ) {

            /* 读取配置的splash图片对应url */
            if( sPreferenceLoader == null ) {
                  sPreferenceLoader = new PreferenceLoader(
                      context,
                      SPLASH_CONFIG
                  );
            }

            sSplashImageUrl = sPreferenceLoader.getString( SPLASH_IMAGE_CACHED_URL );

            Log.e( TAG, "setSplashImage : sSplashImageUrl " + sSplashImageUrl );

            if( sSplashImageUrl == null ) {
                  /* 如果没有配置的url,那么去缓存一下,用于下一次设置splashImage */
                  updateSplashImageUrlForNextTime();
                  return;
            }

            /* 更新一下splashUrl配置,用于下次操作时读取 */
            updateSplashImageUrlForNextTime();
      }

      /**
       * 更新新的 splash 图片
       */
      private static void updateSplashImageUrlForNextTime ( ) {

      }
}
