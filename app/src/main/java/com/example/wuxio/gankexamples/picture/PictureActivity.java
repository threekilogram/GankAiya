package com.example.wuxio.gankexamples.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.main.BeautyModel;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.threekilogram.systemui.SystemUi;
import java.util.List;
import tech.threekilogram.watcher.ImageWatcherView;
import tech.threekilogram.watcher.ImageWatcherView.ImageWatcherAdapter;
import tech.threekilogram.watcher.ImageWatcherView.ScaleImageViewHolder;
import tech.threekilogram.watcher.ScaleImageView;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      private static final String TAG = PictureActivity.class.getSimpleName();

      private ImageWatcherView mImageWatcher;
      private WatcherAdapter   mAdapter;

      /**
       * @param startIndex 图片数据起始索引
       * @param currentIndex 图片当前显示索引
       * @param bitmaps 缓存的图片
       */
      public static void start (
          MainActivity activity,
          int startIndex,
          int currentIndex,
          List<Bitmap> bitmaps ) {

            PictureModel.bind( startIndex, currentIndex, bitmaps );

            /* quitApp activity */
            Intent starter = new Intent( activity, PictureActivity.class );
            activity.startActivity(
                starter
            );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            PictureModel.bind( this );
            super.setContentView( R.layout.activity_picture );

            initView();
            SystemUi.immersive( this );
      }

      private void initView ( ) {

            mImageWatcher = findViewById( R.id.imageWatcher );
            mAdapter = new WatcherAdapter();
            mImageWatcher.setImageWatcherAdapter( mAdapter );
            mImageWatcher.scrollToPosition( PictureModel.getCurrentIndex() );
      }

      private void setCurrentIndexState ( ) {

      }

      public void notifyItemChanged ( int position ) {

            mAdapter.notifyItemChanged( position );
      }

      private View createHolderView ( ViewGroup parent ) {

            return LayoutInflater.from( this )
                                 .inflate(
                                     R.layout.activity_picture__watcher_item,
                                     parent,
                                     false
                                 );
      }

      /**
       * image watcher adapter
       */
      private class WatcherAdapter extends ImageWatcherAdapter {

            private List<String> mUrls = BeautyModel.getUrls();

            @Override
            protected Bitmap getBitmapForScaleImageItem ( int position ) {

                  return PictureModel.loadBitmap( position, mUrls.get( position ) );
            }

            @Override
            public int getItemCount ( ) {

                  return mUrls.size();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder (
                @NonNull ViewGroup parent, int viewType ) {

                  return new ScaleHolder( createHolderView( parent ) );
            }
      }

      private class ScaleHolder extends ScaleImageViewHolder {

            ScaleImageView mScaleImageView;
            ProgressBar    mProgressBar;

            private ScaleHolder ( View itemView ) {

                  super( itemView );
                  mScaleImageView = itemView.findViewById( R.id.scale );
                  mProgressBar = itemView.findViewById( R.id.progress );
                  itemView.setTag( R.id.item_holder, this );
            }

            @Override
            protected void onBitmapNotNull (
                int position, ScaleImageView imageView, Bitmap bitmap ) {

                  mProgressBar.setVisibility( View.GONE );
                  imageView.reset();
                  super.onBitmapNotNull( position, imageView, bitmap );
            }

            @Override
            protected void onBitmapNull (
                int position, ScaleImageView imageView ) {

                  imageView.setImageBitmap( null );
                  imageView.reset();
                  mProgressBar.setVisibility( android.view.View.VISIBLE );

                  PictureModel.loadBitmapFromCache( position, mAdapter.mUrls.get( position ) );
            }

            @NonNull
            @Override
            protected ScaleImageView findScaleImageView ( ) {

                  return mScaleImageView;
            }
      }
}
