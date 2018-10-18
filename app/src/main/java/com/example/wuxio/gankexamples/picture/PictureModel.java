package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.lang.ref.WeakReference;
import java.util.List;
import tech.threekilogram.screen.ScreenSize;

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
            return BitmapManager.loadBitmapFromMemory( url );
      }

      static void loadBitmapFromCache ( int position, String url ) {

            PoolExecutor.execute( ( ) -> {

                  if( BitmapManager.hasPictureCache( url ) ) {
                        loadFromFile( position, url );
                  } else {
                        /* 缓存失效 */
                        BitmapManager.downLoadPicture( url );
                        loadFromFile( position, url );
                  }
            } );
      }

      private static void loadFromFile ( int position, String url ) {

            if( BitmapManager.loadBitmapFromMemory( url ) != null ) {
                  try {
                        sRef.get().notifyItemChanged( position );
                        return;
                  } catch(Exception e) {
                        /* nothing worry about */
                  }
            }

            BitmapManager.loadBitmapFromFile(
                url,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
            );
            if( BitmapManager.loadBitmapFromMemory( url ) != null ) {
                  try {
                        sRef.get().notifyItemChanged( position );
                  } catch(Exception e) {
                        /* nothing worry about */
                  }
            } else {

                  try {
                        sRef.get().notifyItemCantGetBitmap( position );
                  } catch(Exception e) {
                        /* nothing worry about */
                  }
            }
      }
}
