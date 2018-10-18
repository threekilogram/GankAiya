package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import java.lang.ref.WeakReference;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.model.preference.PreferenceLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-09-11:9:08
 */
public class SplashModel {

      /**
       * splash preference name
       */
      private static final String PREFERENCE_NAME = "splash_config";
      /**
       * splash image logo url key
       */
      private static final String KEY_SPLASH_URL  = "splash_image_cached_url";

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

      /**
       * 绑定宿主
       */
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
                      PREFERENCE_NAME
                  );
            }

            sSplashImageUrl = sPreferenceLoader.getString( KEY_SPLASH_URL );

            if( sSplashImageUrl != null ) {
                  if( BitmapManager.hasPictureCache( sSplashImageUrl ) ) {

                        /* 设置图片 */
                        Bitmap bitmap = BitmapManager.loadBitmap(
                            sSplashImageUrl,
                            ScreenSize.getWidth(),
                            ScreenSize.getHeight()
                        );

                        if( bitmap != null ) {
                              sRef.get().mLogoImage.setImageBitmap( bitmap );
                        } else {
                              cachePicture();
                        }
                  } else {
                        cachePicture();
                  }
            }

            /* 更新一下splashUrl配置,用于下次操作时读取 */
            updateSplashImageUrlForNextTime();
      }

      private static void cachePicture ( ) {

            PoolExecutor.execute(
                ( ) -> BitmapManager.downLoadPicture( sSplashImageUrl ) );
      }

      /**
       * 更新新的 splash 图片
       */
      private static void updateSplashImageUrlForNextTime ( ) {

            if( !NetWork.hasNetwork() ) {
                  ToastMessage.toast( "没有网络" );
                  return;
            }

            PoolExecutor.execute( ( ) -> {

                  GankCategory c = BeanLoader
                      .loadLatestCategoryJson( GankUrl.category( GankUrl.BEAUTY, 1, 1 ) );

                  try {

                        String url = c.getResults().get( 0 ).getUrl();

                        if( url != null && !url.equals( sSplashImageUrl ) ) {

                              sPreferenceLoader.save( KEY_SPLASH_URL, url );
                              BitmapManager.downLoadPicture( url );
                        }
                  } catch(Exception e) {
                        /* nothing */
                  }
            } );
      }
}
