package com.example.wuxio.gankexamples.splash;

import android.util.Log;
import android.widget.ImageView;
import com.example.wuxio.gankexamples.model.GankModel;
import java.lang.ref.WeakReference;
import tech.threekilogram.depository.preference.PreferenceLoader;

/**
 * @author Liujin 2018-09-11:9:08
 */
public class SplashModel {

      private static final String TAG = SplashModel.class.getSimpleName();

      private static final String SPLASH_CONFIG           = "splash_config";
      private static final String SPLASH_IMAGE_CACHED_URL = "splash_image_cached_url";

      private static String           sSplashImageUrl;
      private static PreferenceLoader sPreferenceLoader;

      /**
       * 为{@link SplashActivity#mLogoImage}设置图片
       *
       * @param view view
       * @param bitmapWidth width
       * @param bitmapHeight height
       */
      static void setSplashImage ( ImageView view, int bitmapWidth, int bitmapHeight ) {

            /* 读取配置的splash图片对应url */
            if( sPreferenceLoader == null ) {
                  sPreferenceLoader = new PreferenceLoader(
                      view.getContext().getApplicationContext(), SPLASH_CONFIG );
            }

            if( sSplashImageUrl == null ) {
                  sSplashImageUrl = sPreferenceLoader.getString( SPLASH_IMAGE_CACHED_URL );
            }

            if( sSplashImageUrl == null ) {
                  /* 如果没有配置的url,那么去缓存一下,用于下一次设置splashImage */
                  Log.e( TAG, "setSplashImage : null splash image url" );
                  updateNextSplashImageUrl();
                  return;
            }

            Log.e( TAG, "setSplashImage : splash url " + sSplashImageUrl );

            /* 从数据层根据url读取bitmap */
            WeakReference<ImageView> ref = new WeakReference<>( view );
            GankModel.loadBitmap(
                sSplashImageUrl,
                bitmapWidth,
                bitmapHeight,
                ( url, bitmap ) -> {
                      if( bitmap != null ) {
                            ImageView imageView = ref.get();
                            if( imageView == null ) {
                                  return;
                            }
                            imageView.setImageBitmap( bitmap );
                      }
                }
            );

            /* 更新一下splashUrl配置,用于下次操作时读取 */
            updateNextSplashImageUrl();
      }

      /**
       * 更新新的 splash 图片
       */
      private static void updateNextSplashImageUrl ( ) {

            GankModel.loadBeautyItem( 0, ( category, index, item ) -> {

                  if( item != null ) {
                        String url = item.getUrl();
                        Log.e( TAG, "updateNextSplashImageUrl : " + url );
                        if( !url.equals( sSplashImageUrl ) ) {
                              /* 有新的图片,下载它 */
                              cacheNextSplashImage( url );
                        }
                  }
            } );
      }

      private static void cacheNextSplashImage ( String url ) {

            GankModel.downLoadImage( url, ( url1, file ) -> {

                  /* 下载成功,更新配置 */
                  if( file.exists() ) {
                        sPreferenceLoader.save( SPLASH_IMAGE_CACHED_URL, url1 );
                  }
            } );
      }
}
