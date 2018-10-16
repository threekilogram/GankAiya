package com.example.wuxio.gankexamples.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.widget.RecyclerFlingChangeView;
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
      protected SwipeRefreshLayout      mSwipeRefresh;
      private   ShowAdapter             mAdapter;
      private   String                  mCategory;
      private   ObjectBus               mBus = ObjectBus.newFixSizeQueue( 30 );
      private   CategoryModel           mCategoryModel;

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

            if( sDefaultGif == null ) {
                  sDefaultGif = BitmapFactory.decodeResource( getResources(), R.drawable.wait );
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

            return inflater.inflate( R.layout.fragment_show, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( View rootView ) {

            mSwipeRefresh = rootView.findViewById( R.id.swipeRefresh );
            /* 打开界面 刷新 */
            mSwipeRefresh.setRefreshing( true );

            mRecycler = rootView.findViewById( R.id.recycler );
            LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
            mRecycler.setLayoutManager( layoutManager );
            mRecycler.setFlingScale( 0.4f );
            mRecycler.setItemAnimator( null );
            mAdapter = new ShowAdapter();
            mRecycler.setAdapter( mAdapter );

            /* 模拟刷新 */
            WeakReference<SwipeRefreshLayout> ref = new WeakReference<>( mSwipeRefresh );
            mSwipeRefresh.setOnRefreshListener( ( ) -> {
                  mSwipeRefresh.postDelayed(
                      ( ) -> {
                            try {
                                  ref.get().setRefreshing( false );
                            } catch(Exception e) {
                                  /* nothing worry about */
                            }
                      },
                      2000
                  );
            } );
      }

      public void onSelected ( ) {

            Log.e( TAG, "onSelected : " );
            mRecycler.post( ( ) -> {

                  setAdapterData( mAdapter );
            } );
      }

      public void onUnSelected ( ) {

            Log.e( TAG, "onUnSelected : " );
      }

      public void onReselected ( ) {

            Log.e( TAG, "onReselected : " );
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
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefresh.setRefreshing( false );
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

            private GifImageView mGifImageView;
            private TextView     mDesc;
            private TextView     mWho;
            private int          mBindPosition;

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
                  } else {

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
}
