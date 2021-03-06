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
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import com.threekilogram.objectbus.executor.PoolExecutor;
import com.threekilogram.systemui.SystemUi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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
      private PermissionResult mOnRequestPermissionResult;

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

                  int mPosition;

                  @Override
                  protected void onScroll (
                      int state, int currentPosition, int nextPosition, int dx, int dy ) {

                        super.onScroll( state, currentPosition, nextPosition, dx, dy );

                        if( nextPosition != mPosition ) {
                              String text = ( nextPosition + 1 ) + "/" + mAdapter.mUrls.size();
                              mIndex.setText( text );
                        }
                        mPosition = nextPosition;
                  }
            } );

            String text = ( currentIndex + 1 ) + "/" + mAdapter.mUrls.size();
            mIndex.setText( text );

            mSave.setOnClickListener( v -> {

                  int position = mImageWatcher.getCurrentPosition();
                  String url = mAdapter.mUrls.get( position );

                  if( mOnRequestPermissionResult == null ) {

                        mOnRequestPermissionResult = new PermissionResult();
                  }
                  mOnRequestPermissionResult.setUrl( url );
                  PermissionManager.request(
                      PictureActivity.this,
                      permission.WRITE_EXTERNAL_STORAGE,
                      mOnRequestPermissionResult
                  );
            } );
      }

      public void notifyItemChanged ( int position ) {

            mAdapter.notifyItemChanged( position );
      }

      public void notifyItemCantGetBitmap ( int position ) {

            ViewHolder holder = mImageWatcher.getRecyclerView()
                                             .findViewHolderForLayoutPosition(
                                                 position );
            if( holder != null ) {
                  ( (ScaleHolder) holder ).onBitmapCantGet( position );
            }
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

            public void setUrl ( String url ) {

                  mUrl = url;
            }

            public String getUrl ( ) {

                  return mUrl;
            }

            @Override
            public void onSuccess ( String permission ) {

                  File file = BitmapManager.getFile( mUrl );
                  File directory = Environment
                      .getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
                  File result = new File( directory, file.getName() );

                  if( result.exists() ) {
                        ToastMessage.toast( "已经保存" );
                        return;
                  }

                  if( file.exists() ) {
                        PoolExecutor.execute( ( ) -> {

                              try {
                                    FileInputStream inputStream = new FileInputStream( file );

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

            protected void onBitmapCantGet ( int position ) {

                  ToastMessage.toast( "图片失效" );
            }

            @NonNull
            @Override
            protected ScaleImageView findScaleImageView ( ) {

                  return mScaleImageView;
            }
      }
}
