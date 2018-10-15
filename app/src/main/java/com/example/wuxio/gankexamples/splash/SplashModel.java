package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.lang.ref.WeakReference;
import tech.threekilogram.depository.preference.PreferenceLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-09-11:9:08
 */
public class SplashModel {

      private static final String TAG             = SplashModel.class.getSimpleName();
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

            Log.e( TAG, "setSplashImage : 配置的splash图片地址: " + sSplashImageUrl );

            if( sSplashImageUrl != null ) {
                  if( BitmapCache.hasPictureCache( sSplashImageUrl ) ) {

                        /* 设置图片 */
                        Bitmap bitmap = BitmapCache.loadBitmap(
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
                        Log.e( TAG, "setSplashImage : splash图片缓存失效,重新下载" );
                        cachePicture();
                  }
            }

            /* 更新一下splashUrl配置,用于下次操作时读取 */
            updateSplashImageUrlForNextTime();
      }

      private static void cachePicture ( ) {

            PoolExecutor.execute(
                ( ) -> BitmapCache.downLoadPicture( sSplashImageUrl ) );
      }

      /**
       * 更新新的 splash 图片
       */
      private static void updateSplashImageUrlForNextTime ( ) {

            if( !NetWork.hasNetwork() ) {
                  ToastMessage.toast( "没有网络" );
            }

            PoolExecutor.execute( ( ) -> {

                  GankCategory c = BeanLoader.loadLatestCategoryJson( GankUrl.beautyLatestUrl() );
                  Log.e(
                      TAG, "updateSplashImageUrlForNextTime : 获取到最新的福利数据 " + c );

                  try {

                        String url = c.getResults().get( 0 ).getUrl();
                        Log.e( TAG, "updateSplashImageUrlForNextTime : 获取到最新的splash地址 " + url );

                        if( url != null && !url.equals( sSplashImageUrl ) ) {

                              sPreferenceLoader.save( KEY_SPLASH_URL, url );
                              Log.e(
                                  TAG, "updateSplashImageUrlForNextTime : 更新最新的splash地址 " + url );

                              BitmapCache.downLoadPicture( url );
                        }
                  } catch(Exception e) {

                        Log.e( TAG, "updateSplashImageUrlForNextTime : 配置最新的splash地址异常" );
                  }
            } );
      }
}
