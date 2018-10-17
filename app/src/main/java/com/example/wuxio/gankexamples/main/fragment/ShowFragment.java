package com.example.wuxio.gankexamples.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.widget.RecyclerFlingChangeView;
import com.threekilogram.drawable.widget.StaticAnimateDrawableView;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

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
      private   ObjectBus               mBus = ObjectBus.newFixSizeQueue( 30 );
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
                  setUrls( adapter );
            }
      }

      private void setUrls ( ShowAdapter adapter ) {

            WeakReference<ShowAdapter> ref = new WeakReference<>( adapter );

            PoolExecutor.execute( ( ) -> {
                  List<String> urls = mCategoryModel.getLocalBeanUrls();
                  if( urls != null && urls.size() > 0 ) {

                        try {
                              ref.get().setUrls( urls );
                        } catch(Exception e) {
                              /* nothing at there */
                        }
                  }
            } );
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

            void setGif ( int position, File gifFile ) {

                  itemView.post( ( ) -> {
                        if( position == mBindPosition ) {
                              mGifImageView.setImageURI( Uri.fromFile( gifFile ) );
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
                              setShowHolderGif( position, images.get( 0 ), this );
                        } else {
                              mGifImageView.setVisibility( View.GONE );
                              mGifImageView.setImageBitmap( sDefaultGif );
                        }
                  }
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
