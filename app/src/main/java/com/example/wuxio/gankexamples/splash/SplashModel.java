package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.log.AppLog;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.bus.ObjectBus;
import java.lang.ref.WeakReference;
import tech.threekilogram.model.preference.PreferenceLoader;
import tech.threekilogram.model.stream.StreamLoader;
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
       * 图片
       */
      private static Bitmap                        sSplashBitmap;

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

            ObjectBus bus = ObjectBus.newList();

            bus.toPool( ( ) -> {

                  readConfigSplashUrl();

                  if( sSplashImageUrl != null ) {

                        loadSplashBitmapByConfigUrl( bus );
                  }
            } ).toMain( ( ) -> {

                  if( sSplashBitmap != null ) {
                        try {
                              sRef.get().mLogoImage.setImageBitmap( sSplashBitmap );
                        } catch(Exception e) {
                              /* nothing */
                        }
                  }
                  sSplashBitmap = null;
            } ).toPool( SplashModel::prepareNewSplashPicture ).run();
      }

      /**
       * 查询是否有新的splash图片需要更新
       */
      private static void prepareNewSplashPicture ( ) {

            if( !NetWork.hasNetwork() ) {
                  return;
            }

            GankCategory c = StreamLoader.loadJsonFromNet(
                GankUrl.category( GankUrl.BEAUTY, 1, 1 ),
                GankCategory.class
            );

            try {

                  String url = c.getResults().get( 0 ).getUrl();

                  if( url != null && !url.equals( sSplashImageUrl ) ) {

                        sPreferenceLoader.save( KEY_SPLASH_URL, url );
                        BitmapManager.downLoadPicture( url );
                        AppLog.addLog(
                            "splash 更新图片: " + url + " " + BitmapManager.getFile( url ) );
                  } else {
                        AppLog.addLog(
                            "splash 不需要更新图片" );
                  }
            } catch(Exception e) {
                  /* nothing */

                  AppLog.addLog(
                      "splash 获取最新的一条福利数据异常" + e.getMessage() );
            }
      }

      /**
       * 根据配置的url从本地缓存加载bitmap,如果缓存失效,那么重新下载
       */
      private static void loadSplashBitmapByConfigUrl ( ObjectBus bus ) {

            if( BitmapManager.hasPictureCache( sSplashImageUrl ) ) {

                  Bitmap bitmap = BitmapManager.loadBitmap(
                      sSplashImageUrl,
                      ScreenSize.getWidth(),
                      ScreenSize.getHeight()
                  );

                  if( bitmap != null ) {

                        sSplashBitmap = bitmap;
                        AppLog.addLog(
                            "splash 设置缓存图片: " + BitmapManager.getFile( sSplashImageUrl ) );
                  }
            } else {

                  if( NetWork.hasNetwork() ) {
                        BitmapManager.downLoadPicture( sSplashImageUrl );
                        AppLog.addLog(
                            "splash 缓存图片失效: 重新下载图片: " + sSplashImageUrl + " "
                                + BitmapManager.getFile( sSplashImageUrl )
                        );
                  } else {
                        AppLog.addLog( "缓存图片失效: 没有网络下载图片" );
                  }
            }
      }

      /**
       * 读取配置的splash 图片 url
       */
      private static void readConfigSplashUrl ( ) {

            if( sPreferenceLoader == null ) {
                  sPreferenceLoader = new PreferenceLoader(
                      App.INSTANCE,
                      PREFERENCE_NAME
                  );
            }

            sSplashImageUrl = sPreferenceLoader.getString( KEY_SPLASH_URL );
            AppLog.addLog( "splash 页面已配置url : " + sSplashImageUrl );
      }
}
