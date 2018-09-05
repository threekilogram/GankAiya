package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.lang.ref.WeakReference;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.depository.cache.json.ObjectLoader;
import tech.threekilogram.depository.function.Doing;
import tech.threekilogram.depository.preference.Preference;

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
      private static ObjectBus    sObjectBus;
      /**
       * 图片加载
       */
      private static BitmapLoader sBitmapLoader;
      /**
       * 防止重复加载
       */
      private static Doing        sDoing;

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

            if( sDoing == null ) {
                  sDoing = new Doing();
            }

            if( sPreference == null ) {
                  sPreference = new Preference( context, GANK_CONFIG );
            }
      }

      /**
       * 先尝试从本地加载url对应图片,存在该图片设置给imageView,并更新最新的图片
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

            /* 更新本地配置图片,用于下次加载时使用 */
            PoolExecutor.execute( ( ) -> {

                  String url = GankUrl.splashImageUrl();
                  sDoing.isRunning( url );
                  GankCategory gankCategory = ObjectLoader.loadFromNet( url, GankCategory.class );
                  sDoing.remove( url );
                  url = gankCategory.getResults().get( 0 ).getUrl();
                  if( !sBitmapLoader.containsOf( url ) ) {
                        sDoing.isRunning( url );
                        sBitmapLoader.downLoad( url );
                        sDoing.remove( url );
                  }
            } );
      }
}
