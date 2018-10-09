package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.example.wuxio.gankexamples.R;
import java.util.List;
import tech.threekilogram.pager.image.ImageWatcherPager;
import tech.threekilogram.pager.image.ImageWatcherPager.ImageViewHolder;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      private ImageWatcherPager mImageWatcher;
      private int               mStartIndex;

      public static void start ( Context context, int startIndex, List<Bitmap> bitmaps ) {

            Intent starter = new Intent( context, PictureActivity.class );
            context.startActivity( starter );

            PictureModel.setStartData( startIndex, bitmaps );
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
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      private class WatcherAdapter extends ImageWatcherPager.ImageWatcherAdapter {

            @Override
            protected Bitmap getImage ( int position ) {

                  return BitmapFactory.decodeResource( getResources(), R.drawable.a42 );
            }

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder (
                @NonNull ViewGroup parent, int viewType ) {

                  return super.onCreateViewHolder( parent, viewType );
            }

            @Override
            public int getItemCount ( ) {

                  return 5;
            }
      }
}
