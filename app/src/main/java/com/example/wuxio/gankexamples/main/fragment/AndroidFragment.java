package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Liujin 2018-10-15:20:54
 */
public class AndroidFragment extends ShowFragment {

      private static final String TAG = AndroidFragment.class.getSimpleName();

      private ObjectBus mBus = ObjectBus.newFixSizeQueue( 30 );

      public static AndroidFragment newInstance ( ) {

            return new AndroidFragment();
      }

      @Override
      protected void setAdapterData ( ShowAdapter adapter ) {

            List<String> urls = adapter.getUrls();
            if( urls == null || urls.size() == 0 ) {
                  setUrls( adapter );
            }
      }

      private void setUrls ( ShowAdapter adapter ) {

            WeakReference<ShowAdapter> ref = new WeakReference<>( adapter );

            PoolExecutor.execute( ( ) -> {
                  List<String> urls = AndroidModel.getAndroidLocalBeanUrls();
                  if( urls != null && urls.size() > 0 ) {

                        try {
                              ref.get().setUrls( urls );
                        } catch(Exception e) {
                              /* nothing at there */
                        }
                  }
            } );
      }

      @Override
      protected void setShowHolderData ( ShowHolder holder, int position ) {

            GankCategoryItem item = AndroidModel.getItemFromMemory( position );
            if( item != null ) {
                  if( holder.getBindPosition() == position ) {
                        holder.bind( position, item );
                  }
                  return;
            }

            WeakReference<ShowHolder> ref = new WeakReference<>( holder );
            mBus.toPool( ( ) -> {

                  GankCategoryItem loaded = AndroidModel.getItem( position );
                  if( loaded != null ) {
                        try {
                              ref.get().setGankCategoryItem( position, loaded );
                        } catch(Exception e) {
                              /* nothing at there */
                        }
                  }
            } ).run();
      }

      @Override
      protected void setShowHolderGif (
          int position, String url, ShowHolder holder ) {

            loadGif( position, url, new WeakReference<>( holder ) );
      }

      private void loadGif ( int position, String url, WeakReference<ShowHolder> ref ) {

            mBus.toPool( ( ) -> {
                  File file = BitmapCache.downLoadPicture( url );
                  if( file.exists() ) {
                        try {
                              ref.get().setGif( position, file );
                        } catch(Exception e) {
                              /* nothing at there */
                        }
                  }
            } ).run();
      }
}
