package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.util.Log;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.GankModel;
import java.lang.ref.WeakReference;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class MainModel {

      private static final String TAG = MainModel.class.getSimpleName();
      private static WeakReference<MainActivity> sHost;

      public static void bind ( MainActivity activity ) {

            sHost = new WeakReference<>( activity );
      }

      public static void loadBannerBitmap ( Context context ) {

            int width = ScreenSize.getWidth();
            int height = ScreenSize.resToPx( context, R.dimen.main_appbar_height );

            GankModel.loadListBitmaps(
                0, 5,
                width, height,
                result -> {

                      Log.e( TAG, "loadBannerBitmap : " + result );

                      MainActivity mainActivity = sHost.get();

                      if( result == null || result.size() == 0 ) {
                            mainActivity.setBannerNoResource();
                      } else {
                            mainActivity.setBannerBitmaps( result );
                      }
                }
            );
      }
}
