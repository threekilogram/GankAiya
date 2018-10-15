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
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.widget.LoadingView;
import com.example.wuxio.gankexamples.widget.RecyclerFlingChangeView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author wuxio 2018-04-29:9:23
 */
public abstract class ShowFragment extends Fragment {

      private static final String TAG = ShowFragment.class.getSimpleName();

      protected static Bitmap sDefaultGif;

      protected View                    rootView;
      protected RecyclerFlingChangeView mRecycler;
      protected SwipeRefreshLayout      mSwipeRefresh;
      private   ShowAdapter             mAdapter;

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

            rootView = inflater.inflate( R.layout.fragment_show, container, false );
            initView( rootView );
            return rootView;
      }

      private void initView ( View rootView ) {

            mSwipeRefresh = rootView.findViewById( R.id.swipeRefresh );
            mRecycler = rootView.findViewById( R.id.recycler );
            LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
            mRecycler.setLayoutManager( layoutManager );
            mRecycler.setFlingScale( 0.4f );
            mRecycler.setItemAnimator( null );
            mAdapter = new ShowAdapter();
            mRecycler.setAdapter( mAdapter );

            /* 打开界面 刷新 */
            mSwipeRefresh.setRefreshing( true );

            /* 防止泄露 */
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
            rootView.post( ( ) -> {

                  setAdapterData( mAdapter );
            } );
      }

      public void onUnSelected ( ) {

            Log.e( TAG, "onUnSelected : " );
      }

      public void onReselected ( ) {

            Log.e( TAG, "onReselected : " );
      }

      /**
       * 为{@link ShowAdapter#mUrls}设置数据,设置好数据之后,记得调用{@link ShowAdapter#setUrls(List)}
       *
       * @param adapterData fragment adapter
       */
      protected abstract void setAdapterData ( ShowAdapter adapterData );

      /**
       * 为{@link ShowHolder}设置位于该位置的数据
       *
       * @param holder holder
       * @param position holder位置
       */
      protected abstract void setShowHolderData ( ShowHolder holder, int position );

      /**
       * 为{@link ShowHolder}设置位于该位置的gif数据
       *
       * @param position 位置
       * @param url gif url
       * @param holder 需要设置的holder
       */
      protected abstract void setShowHolderGif ( int position, String url, ShowHolder holder );

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
      protected class ShowHolder extends ViewHolder {

            private GifImageView mGifImageView;
            private TextView     mDesc;
            private TextView     mWho;
            private LoadingView  mLoadingView;
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
                  mLoadingView = itemView.findViewById( R.id.loadingView );
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
                        mLoadingView.setVisibility( View.VISIBLE );
                  } else {

                        mDesc.setVisibility( View.VISIBLE );
                        mWho.setVisibility( View.VISIBLE );
                        mLoadingView.setVisibility( View.INVISIBLE );

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
