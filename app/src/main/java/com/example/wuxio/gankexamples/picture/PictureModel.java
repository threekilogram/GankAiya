package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Liujin 2018-10-14:15:40
 */
public class PictureModel {

      private static final String TAG = PictureModel.class.getSimpleName();

      private static int                            sStartIndex;
      private static int                            sCurrentIndex;
      private static List<Bitmap>                   sBitmaps;
      private static WeakReference<PictureActivity> sRef;

      static void bind ( int startIndex, int currentIndex, List<Bitmap> bitmaps ) {

            sStartIndex = startIndex;
            sCurrentIndex = currentIndex;
            sBitmaps = bitmaps;

            Log.e( TAG, "bind : picture 初始状态 " + sStartIndex + " " + sCurrentIndex );
      }

      static void bind ( PictureActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      public static int getCurrentIndex ( ) {

            return sStartIndex + sCurrentIndex;
      }

      static Bitmap loadBitmap ( int position, String url ) {

            if( sStartIndex <= position && position < sStartIndex + sBitmaps.size() ) {
                  return sBitmaps.get( position - sStartIndex );
            }
            return null;
      }
}
