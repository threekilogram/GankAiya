package com.example.wuxio.gankexamples.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.wuxio.gankexamples.R;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment {

      private static final String TAG = ShowFragment.class.getSimpleName();

      protected View               rootView;
      protected RecyclerView       mRecycler;
      protected SwipeRefreshLayout mSwipeRefresh;
      private   String             mCategory;

      public static ShowFragment newInstance ( String category ) {

            ShowFragment fragment = new ShowFragment();
            fragment.mCategory = category;
            return fragment;
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
      }

      public void onUnSelected ( ) {

            Log.e( TAG, "onUnSelected : " + mCategory );
      }

      public void onReselected ( ) {

            Log.e( TAG, "onReselected : " + mCategory );
      }
}
