package com.example.wuxio.gankexamples.main;

import android.content.Context;
import com.example.wuxio.gankexamples.R;
import java.lang.ref.WeakReference;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class PictureModel {

      private static final String TAG = PictureModel.class.getSimpleName();

      private static WeakReference<MainActivity> sRef;

      public static void bind ( MainActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      public static void loadBannerBitmap ( Context context ) {

            int width = ScreenSize.getWidth();
            int height = ScreenSize.resToPx( context, R.dimen.main_appbar_height );
      }
}
