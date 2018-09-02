package com.example.wuxio.gankexamples.model;

import android.content.Context;
import android.widget.ImageView;
import com.threekilogram.objectbus.bus.ObjectBus;
import tech.threekilogram.depository.bitmap.BitmapLoader;
import tech.threekilogram.depository.json.GsonConverter;
import tech.threekilogram.depository.json.JsonLoader;
import tech.threekilogram.depository.preference.Preference;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-08-15
 * @time: 17:21
 */
public class GankModel {

      private static final String TAG = GankModel.class.getSimpleName();

      private static ObjectBus                    sObjectBus;
      private static BitmapLoader                 sBitmapLoader;
      private static JsonLoader<GankCategoryBean> sJsonLoader;
      private static Preference                   sPreference;

      private static final String TODAY_IMAGE_URL = "today_image_url";

      public static void init ( Context context ) {

            sObjectBus = ObjectBus.newList();

            sBitmapLoader = new BitmapLoader(
                (int) Runtime.getRuntime().maxMemory() >> 3,
                context.getExternalFilesDir( "gankImage" )
            );

            sJsonLoader = new JsonLoader<>(
                context.getExternalFilesDir( "gankBean" ),
                new GsonConverter<>( GankCategoryBean.class )
            );

            sPreference = new Preference( context, "gankConfig" );
      }

      /**
       * 为{@link com.example.wuxio.gankexamples.splash.SplashActivity}设置展示图片
       */
      public static void prepareSplashImage ( ImageView view, int width, int height ) {

      }
}
