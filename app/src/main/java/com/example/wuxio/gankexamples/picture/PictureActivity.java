package com.example.wuxio.gankexamples.picture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.wuxio.gankexamples.R;
import java.util.List;
import tech.threekilogram.scalegesture.ImageWatcherView;
import tech.threekilogram.scalegesture.ImageWatcherView.ImageWatcherAdapter;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      private ImageWatcherView mImageWatcher;

      public static void start (
          Activity context, View view, int startIndex, List<Bitmap> bitmaps ) {

            /* set data */
            PictureModel.setStartData( startIndex, bitmaps );

            /* start activity */
            Intent starter = new Intent( context, PictureActivity.class );
            context.startActivity(
                starter,
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation( context, view, view.getTransitionName() )
                    .toBundle()
            );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_picture );

            PictureModel.bind( this );
            initView();
      }

      private void initView ( ) {

            mImageWatcher = findViewById( R.id.imageWatcher );
            mImageWatcher.setImageWatcherAdapter( new WatcherAdapter() );
            mImageWatcher.getRecyclerView().scrollToPosition( PictureModel.getStartIndex() );
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
            PictureModel.onHostDestroy();
      }

      /**
       * image watcher adapter
       */
      private class WatcherAdapter extends ImageWatcherAdapter {

            private List<Bitmap> mBitmaps = PictureModel.getBitmaps();

            @Override
            protected Bitmap getImage ( int position ) {

                  if( mBitmaps == null ) {
                        mBitmaps = PictureModel.getBitmaps();
                  }

                  return mBitmaps.get( position );
            }

            @Override
            public int getItemCount ( ) {

                  return mBitmaps == null ? 0 : mBitmaps.size();
            }
      }
}
