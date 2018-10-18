package com.example.wuxio.gankexamples.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.web.WebActivity;
import com.example.wuxio.gankexamples.widget.RecyclerFlingChangeView;
import com.threekilogram.drawable.widget.StaticAnimateDrawableView;
import com.threekilogram.objectbus.bus.ObjectBus;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import tech.threekilogram.executor.PoolExecutor;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment {

      private String TAG;

      /**
       * 默认gif图片
       */
      protected static Bitmap sDefaultGif;

      protected RecyclerFlingChangeView mRecycler;
      private   ShowAdapter             mAdapter;
      private   String                  mCategory;
      private   ObjectBus               mBus = ObjectBus.newFixSizeQueue( 100 );
      private   CategoryModel           mCategoryModel;
      private   LinearLayoutManager     mLayoutManager;

      public static ShowFragment newInstance ( String category ) {

            ShowFragment fragment = new ShowFragment();
            fragment.mCategory = category;
            fragment.TAG = ShowFragment.class.getSimpleName() + " " + category;
            fragment.mCategoryModel = CategoryModel.instance( category );
            return fragment;
      }

      @Override
      public void onCreate ( @Nullable Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );

            init();
      }

      private void init ( ) {

            if( sDefaultGif == null ) {
                  sDefaultGif = BitmapFactory.decodeResource( getResources(), R.drawable.wait );
            }

            if( mLayoutManager == null ) {
                  mLayoutManager = new LinearLayoutManager( getContext() );
            }
            if( mAdapter == null ) {
                  mAdapter = new ShowAdapter();
            }
            if( mRecycler == null ) {
                  mRecycler = new RecyclerFlingChangeView( getContext() );

                  mRecycler.setLayoutManager( mLayoutManager );
                  mRecycler.setFlingScale( 0.4f );
                  mRecycler.setItemAnimator( null );
                  mRecycler.setAdapter( new LoadingAdapter() );
            }
      }

      @Override
      public void onDestroy ( ) {

            super.onDestroy();
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return mRecycler;
      }

      public void onSelected ( ) { }

      public void onUnSelected ( ) { }

      public void onReselected ( ) { }

      public void onIdle ( ) {

            setAdapterData( mAdapter );
      }

      protected void setAdapterData ( ShowAdapter adapter ) {

            List<String> urls = adapter.getUrls();
            if( urls == null || urls.size() == 0 ) {
                  WeakReference<ShowAdapter> ref = new WeakReference<>( adapter );

                  PoolExecutor.execute( ( ) -> {
                        List<String> result = mCategoryModel.getLocalBeanUrls();
                        if( result != null && result.size() > 0 ) {

                              try {
                                    ref.get().setUrls( result );
                              } catch(Exception e) {
                                    /* nothing at there */
                              }
                        }
                  } );
            }
      }

      protected void setShowHolderData ( ShowHolder holder, int position ) {

            GankCategoryItem item = mCategoryModel.getItemFromMemory( position );
            if( item != null ) {
                  if( holder.getBindPosition() == position ) {
                        holder.bind( position, item );
                  }
                  return;
            } else {
                  holder.bind( position, null );
            }

            WeakReference<ShowHolder> ref = new WeakReference<>( holder );
            mBus.toPool( ( ) -> {

                  GankCategoryItem loaded = mCategoryModel.getItem( position );
                  if( loaded != null ) {
                        try {
                              ref.get().setGankCategoryItem( position, loaded );
                        } catch(Exception e) {
                              /* nothing at there */
                        }
                  }
            } ).run();
      }

      protected void setShowHolderGif (
          int position, String url, ShowHolder holder, int width, int height ) {

            WeakReference<ShowHolder> ref = new WeakReference<>( holder );
            mBus.toPool( ( ) -> {
                  File file = BitmapManager.downLoadPicture( url );
                  if( file.exists() ) {

                        try {
                              if( ref.get().needDecode( position ) ) {
                                    try {
                                          GifDrawable gifDrawable = new GifDrawable( file );
                                          ref.get().setGif( position, gifDrawable );
                                    } catch(IOException e) {
                                          e.printStackTrace();
                                          Bitmap bitmap = BitmapManager
                                              .loadBitmap( url, width, height );
                                          ref.get().setBitmap( position, bitmap );
                                    }
                              }
                        } catch(Exception e) {
                              /* nothing */
                        }
                  }
            } ).run();
      }

      /**
       * recycler adapter
       */
      protected class ShowAdapter extends Adapter<ShowHolder> {

            private List<String> mUrls;

            List<String> getUrls ( ) {

                  return mUrls;
            }

            void setUrls ( List<String> urls ) {

                  mRecycler.post( ( ) -> {
                        mAdapter.mUrls = urls;
                        mRecycler.setAdapter( mAdapter );
                        mAdapter.notifyDataSetChanged();
                  } );
            }

            @NonNull
            @Override
            public ShowHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  View view = getLayoutInflater()
                      .inflate( R.layout.fragment_show_item, parent, false );
                  return new ShowHolder( view );
            }

            @Override
            public void onBindViewHolder (
                @NonNull ShowHolder holder, int position ) {

                  holder.mBindPosition = position;
                  setShowHolderData( holder, position );
            }

            @Override
            public int getItemCount ( ) {

                  return mUrls == null ? 0 : mUrls.size();
            }
      }

      /**
       * viewHolder
       */
      private class ShowHolder extends ViewHolder {

            private GifImageView              mGifImageView;
            private StaticAnimateDrawableView mLoading;
            private TextView                  mDesc;
            private TextView                  mWho;
            private int                       mBindPosition;

            private ShowHolder ( View itemView ) {

                  super( itemView );
                  initView( itemView );
                  itemView.setOnClickListener( new HolderClickListener( this ) );
            }

            int getBindPosition ( ) {

                  return mBindPosition;
            }

            private void initView ( @NonNull final View itemView ) {

                  mGifImageView = itemView.findViewById( R.id.gifImageView );
                  mDesc = itemView.findViewById( R.id.desc );
                  mWho = itemView.findViewById( R.id.who );
                  mLoading = itemView.findViewById( R.id.loading );
            }

            void setGankCategoryItem ( int position, GankCategoryItem item ) {

                  itemView.post( ( ) -> {
                        if( position == mBindPosition ) {

                              bind( position, item );
                        }
                  } );
            }

            boolean needDecode ( int position ) {

                  return position == mBindPosition;
            }

            void setGif ( int position, Drawable drawable ) {

                  itemView.post( ( ) -> {
                        if( position == mBindPosition ) {
                              mGifImageView.setImageDrawable( drawable );
                        }
                  } );
            }

            void setBitmap ( int position, Bitmap bitmap ) {

                  itemView.post( ( ) -> {
                        if( position == mBindPosition ) {
                              mGifImageView.setImageBitmap( bitmap );
                        }
                  } );
            }

            void bind ( int position, GankCategoryItem item ) {

                  if( item == null ) {

                        mGifImageView.setVisibility( View.INVISIBLE );
                        mGifImageView.setImageBitmap( sDefaultGif );
                        mDesc.setVisibility( View.INVISIBLE );
                        mWho.setVisibility( View.INVISIBLE );
                        mLoading.setVisibility( View.VISIBLE );
                  } else {

                        mLoading.setVisibility( View.INVISIBLE );
                        mDesc.setVisibility( View.VISIBLE );
                        mWho.setVisibility( View.VISIBLE );

                        mDesc.setText( item.getDesc() );
                        mWho.setText( item.getWho() );

                        List<String> images = item.getImages();
                        if( images != null && images.size() > 0 ) {
                              mGifImageView.setVisibility( View.VISIBLE );
                              mGifImageView.setImageBitmap( sDefaultGif );
                              setShowHolderGif(
                                  position,
                                  images.get( 0 ),
                                  this,
                                  mGifImageView.getMeasuredWidth(),
                                  mGifImageView.getMeasuredHeight()
                              );
                        } else {
                              mGifImageView.setVisibility( View.GONE );
                              mGifImageView.setImageBitmap( sDefaultGif );
                        }
                  }
            }
      }

      private class HolderClickListener implements OnClickListener {

            private ShowHolder mHolder;

            public HolderClickListener (
                ShowHolder holder ) {

                  mHolder = holder;
            }

            @Override
            public void onClick ( View v ) {

                  int bindPosition = mHolder.mBindPosition;
                  String url = mAdapter.getUrls().get( bindPosition );
                  WebActivity.start( getContext(), url, mCategory );
            }
      }

      /**
       * recycler 没有数据时的adapter
       */
      private class LoadingAdapter extends RecyclerView.Adapter<LoadingHolder> {

            @NonNull
            @Override
            public LoadingHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  ImageView imageView = new ImageView( parent.getContext() );
                  imageView.setScaleType( ScaleType.CENTER_INSIDE );
                  imageView.setLayoutParams(
                      new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
                  imageView.setImageResource( R.drawable.wait );
                  return new LoadingHolder( imageView );
            }

            @Override
            public void onBindViewHolder (
                @NonNull LoadingHolder holder, int position ) {

                  holder.bind();
            }

            @Override
            public int getItemCount ( ) {

                  return 1;
            }
      }

      /**
       * recycler 没有数据时的adapter的holder
       */
      private class LoadingHolder extends ViewHolder {

            public LoadingHolder ( View itemView ) {

                  super( itemView );
            }

            void bind ( ) {

            }

            void unBind ( ) {

            }
      }
}
