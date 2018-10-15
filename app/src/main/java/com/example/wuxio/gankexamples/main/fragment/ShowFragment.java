package com.example.wuxio.gankexamples.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment {

      private static final String TAG = ShowFragment.class.getSimpleName();

      protected View               rootView;
      protected RecyclerView       mRecycler;
      protected SwipeRefreshLayout mSwipeRefresh;
      private   String             mCategory;
      private   ShowAdapter        mAdapter;

      public static ShowFragment newInstance ( String category ) {

            ShowFragment fragment = new ShowFragment();
            fragment.mCategory = category;
            return fragment;
      }

      @Override
      public void onCreate ( @Nullable Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
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

            /* 打开界面 刷新 */
            mSwipeRefresh.setRefreshing( true );
      }

      public void onSelected ( ) {

            Log.e( TAG, "onSelected : " + mCategory );
            if( mAdapter == null ) {
                  mAdapter = new ShowAdapter();
                  mRecycler.setAdapter( mAdapter );
            }
      }

      public void onUnSelected ( ) {

            Log.e( TAG, "onUnSelected : " + mCategory );
      }

      public void onReselected ( ) {

            Log.e( TAG, "onReselected : " + mCategory );
      }

      private class ShowAdapter extends Adapter<ShowHolder> {

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

                  holder.bind( position );
            }

            @Override
            public int getItemCount ( ) {

                  return 50;
            }
      }

      private class ShowHolder extends ViewHolder {

            private TextView     mDesc;
            private GifImageView mGif00;
            private GifImageView mGif02;
            private GifImageView mGif01;
            private TextView     mDate;
            private TextView     mWho;

            public ShowHolder ( View itemView ) {

                  super( itemView );
                  initView( itemView );
            }

            private void initView ( @NonNull final View itemView ) {

                  mDesc = itemView.findViewById( R.id.desc );
                  mGif00 = itemView.findViewById( R.id.gif00 );
                  mGif02 = itemView.findViewById( R.id.gif02 );
                  mGif01 = itemView.findViewById( R.id.gif01 );
                  mDate = itemView.findViewById( R.id.date );
                  mWho = itemView.findViewById( R.id.who );
            }

            private void bind ( int position ) {

                  if( position % 3 == 0 ) {
                        mGif00.setVisibility( View.GONE );
                        mGif01.setVisibility( View.GONE );
                        mGif02.setVisibility( View.GONE );
                  } else {
                        mGif00.setVisibility( View.VISIBLE );
                        mGif01.setVisibility( View.VISIBLE );
                        mGif02.setVisibility( View.VISIBLE );
                  }
            }
      }
}
