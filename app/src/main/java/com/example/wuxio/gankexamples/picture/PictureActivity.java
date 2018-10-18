package com.example.wuxio.gankexamples.picture;

import android.Manifest.permission;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.main.BeautyModel;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import com.threekilogram.systemui.SystemUi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScrollListener;
import tech.threekilogram.watcher.ImageWatcherView;
import tech.threekilogram.watcher.ImageWatcherView.ImageWatcherAdapter;
import tech.threekilogram.watcher.ImageWatcherView.ScaleImageViewHolder;
import tech.threekilogram.watcher.ScaleImageView;
import teck.threekilogram.permission.OnRequestPermissionResultListener;
import teck.threekilogram.permission.PermissionManager;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      private static final String TAG = PictureActivity.class.getSimpleName();

      private ImageWatcherView mImageWatcher;
      private WatcherAdapter   mAdapter;
      private TextView         mIndex;
      private ImageView        mSave;

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
            SystemUi.transparentStatus( this );
      }

      private void initView ( ) {

            mImageWatcher = findViewById( R.id.imageWatcher );
            mAdapter = new WatcherAdapter();
            mImageWatcher.setImageWatcherAdapter( mAdapter );
            int currentIndex = PictureModel.getCurrentIndex();
            mImageWatcher.scrollToPosition( currentIndex );
            mIndex = findViewById( R.id.index );
            mSave = findViewById( R.id.save );

            mImageWatcher.addOnScrollListener( new RecyclerPagerScrollListener() {

                  @Override
                  protected void onPageSelected ( int currentPosition, int nextPosition ) {

                        super.onPageSelected( currentPosition, nextPosition );
                        String text = ( nextPosition + 1 ) + "/" + mAdapter.mUrls.size();
                        mIndex.setText( text );
                  }
            } );

            String text = ( currentIndex + 1 ) + "/" + mAdapter.mUrls.size();
            mIndex.setText( text );

            mSave.setOnClickListener( v -> {

                  int position = mImageWatcher.getCurrentPosition();
                  String url = mAdapter.mUrls.get( position );

                  PermissionManager.request(
                      PictureActivity.this,
                      permission.WRITE_EXTERNAL_STORAGE,
                      new PermissionResult( url )
                  );
            } );
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

      private static class PermissionResult implements OnRequestPermissionResultListener {

            private String mUrl;

            public PermissionResult ( String url ) {

                  mUrl = url;
            }

            @Override
            public void onSuccess ( String permission ) {

                  File file = BitmapCache.getFile( mUrl );
                  File directory = Environment
                      .getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
                  Log.e( TAG, "onSuccess : " + file + " " + directory );
                  if( file.exists() ) {
                        PoolExecutor.execute( ( ) -> {

                              try {
                                    FileInputStream inputStream = new FileInputStream( file );
                                    File result = new File( directory, file.getName() );
                                    FileOutputStream outputStream = new FileOutputStream(
                                        result
                                    );

                                    byte[] temp = new byte[ 256 ];
                                    int len = 0;
                                    while( ( len = inputStream.read( temp ) ) != -1 ) {
                                          outputStream.write( temp, 0, len );
                                    }

                                    MediaStore.Images.Media.insertImage(
                                        App.INSTANCE.getContentResolver(),
                                        result.getAbsolutePath(),
                                        result.getName(),
                                        null
                                    );

                                    App.INSTANCE.sendBroadcast(
                                        new Intent(
                                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                            Uri.fromFile( result )
                                        )
                                    );

                                    ToastMessage.toast( "保存成功" );
                              } catch(IOException e) {
                                    e.printStackTrace();
                              }
                        } );
                  }
            }

            @Override
            public void onFailed ( String permission ) {

                  Toast.makeText( App.INSTANCE, "保存失败,没有权限", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onFinalDenied ( String permission ) {

                  Toast.makeText( App.INSTANCE, "保存失败,没有权限", Toast.LENGTH_SHORT ).show();
            }
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
                  mProgressBar.setVisibility( View.VISIBLE );

                  PictureModel.loadBitmapFromCache( position, mAdapter.mUrls.get( position ) );
            }

            @NonNull
            @Override
            protected ScaleImageView findScaleImageView ( ) {

                  return mScaleImageView;
            }
      }
}
