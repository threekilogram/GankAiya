package com.example.wuxio.gankexamples.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.threekilogram.systemui.SystemUi;
import java.util.List;
import tech.threekilogram.scalegesture.ImageWatcherView;
import tech.threekilogram.scalegesture.ImageWatcherView.ImageWatcherAdapter;
import tech.threekilogram.scalegesture.ImageWatcherView.ScaleImageViewHolder;
import tech.threekilogram.scalegesture.ScaleImageView;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      private static final String TAG = PictureActivity.class.getSimpleName();

      private static List<Bitmap> sBitmaps;

      private ImageWatcherView mImageWatcher;

      public static void start (
          MainActivity activity, View view, List<Bitmap> data ) {

            sBitmaps = data;

            /* start activity */
            Intent starter = new Intent( activity, PictureActivity.class );
            activity.startActivity(
                starter,
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation( activity, view, view.getTransitionName() )
                    .toBundle()
            );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_picture );

            initView();
            SystemUi.immersive( this );
      }

      private void initView ( ) {

            mImageWatcher = findViewById( R.id.imageWatcher );
            mImageWatcher.setImageWatcherAdapter( new WatcherAdapter() );
      }

      private void setCurrentIndexState ( ) {

      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
            sBitmaps = null;
      }

      /**
       * image watcher adapter
       */
      private class WatcherAdapter extends ImageWatcherAdapter {

            @Override
            protected Bitmap getBitmapForScaleImageItem ( int position ) {

                  return null;
            }

            @Override
            public int getItemCount ( ) {

                  return 0;
            }
      }

      private class ScaleHolder extends ScaleImageViewHolder {

            public ScaleHolder ( View itemView ) {

                  super( itemView );
            }

            @Override
            protected void onBitmapNull (
                int position, ScaleImageView imageView ) {

            }
      }
}
