package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Liujin 2018-10-09:9:39
 */
public class PictureModel {

      private static WeakReference<PictureActivity> sRef;

      private static int          sStartIndex;
      private static List<Bitmap> sBitmaps;

      static void bind ( PictureActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      static void setStartData ( int startIndex, List<Bitmap> bitmaps ) {

            sStartIndex = startIndex;
            sBitmaps = bitmaps;
      }

      public static int getStartIndex ( ) {

            return sStartIndex;
      }

      public static List<Bitmap> getBitmaps ( ) {

            return sBitmaps;
      }

      static void onHostDestroy ( ) {

            sStartIndex = 0;
            sBitmaps = null;
      }
}
