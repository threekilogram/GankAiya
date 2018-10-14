package com.example.wuxio.gankexamples.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.web.WebActivity;
import com.threekilogram.constraint.Constraint;
import com.threekilogram.constraint.ConstraintLayout;
import com.threekilogram.constraint.adapter.BaseConstraintAdapter;
import java.util.List;
import tech.threekilogram.messengers.Messengers;
import tech.threekilogram.messengers.OnMessageReceiveListener;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment implements OnMessageReceiveListener {

      private static final String TAG = "ShowFragment";

      protected View                rootView;
      protected RecyclerView        mRecycler;
      protected SwipeRefreshLayout  mSwipeRefresh;
      private   LinearLayoutManager mLayoutManager;
      private   RecyclerAdapter     mRecyclerAdapter;
      private   String              mCategory;

      public static ShowFragment newInstance ( ) {

            ShowFragment fragment = new ShowFragment();
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
            mLayoutManager = new LinearLayoutManager( getContext() );
            mRecycler.setLayoutManager( mLayoutManager );

            /* 打开界面 刷新 */
            mSwipeRefresh.setRefreshing( true );
            mSwipeRefresh.setOnRefreshListener(

                /* 模拟刷新 */

                ( ) -> Messengers.send( 11, 1000, ShowFragment.this )
            );
      }

      //============================ message receive ============================

      @Override
      public void onDestroy ( ) {

            /* 解注册 */
            Messengers.remove( 11, this );
            super.onDestroy();
      }

      @Override
      public void onReceive ( int what, Object extra ) {

            final int flagStopRefresh = 11;

            /* 结束刷新 */
            if( what == flagStopRefresh ) {
                  mSwipeRefresh.setRefreshing( false );
            }
      }

      public void loadData ( String category ) {

            /* 加载数据 */

            ShowFragmentManager instance = ShowFragmentManager.getInstance();
            instance.register( this );
            mCategory = category;
            instance.loadData( mCategory );
      }

      //============================ load data ============================

      public void dataReady ( List<GankCategory> categoryBeans ) {

            /*  后台加载数据完毕,不再刷新 */

            mSwipeRefresh.setRefreshing( false );

            /* 设置recycler 显示数据 */
            if( categoryBeans != null ) {
                  // mRecyclerAdapter = new RecyclerAdapter( categoryBeans );
                  mRecycler.setAdapter( mRecyclerAdapter );
            }
      }

      /**
       * recycler adapter
       */
      private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            List<GankCategory> mCategoryBeans;
            private LayoutInflater        mInflater;
            private ShowConstraintAdapter mAdapter;
            private View.OnClickListener  mOnClickListener;

            public RecyclerAdapter ( List<GankCategory> categoryBeans ) {

                  this.mCategoryBeans = categoryBeans;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder (
                @NonNull ViewGroup parent, int viewType ) {

                  if( mInflater == null ) {
                        mInflater = LayoutInflater.from( parent.getContext() );
                  }

                  if( viewType == 12 ) {

                        View view = mInflater.inflate(
                            R.layout.item_show_pager_recycler_footer,
                            parent,
                            false
                        );

                        return new FooterHolder( view );
                  } else {

                        View view = mInflater.inflate(
                            R.layout.item_show_pager_recycler,
                            parent,
                            false
                        );

                        if( mOnClickListener == null ) {
                              mOnClickListener = new ShowItemClickListener();
                        }

                        view.setOnClickListener( mOnClickListener );

                        return new Holder( view );
                  }
            }

            @Override
            public void onBindViewHolder ( @NonNull RecyclerView.ViewHolder holder, int position ) {

                  if( position == getItemCount() - 1 ) {

                        ( (FooterHolder) holder ).bind();
                  } else {

                        ( (Holder) holder ).bind( position );
                  }
            }

            @Override
            public int getItemViewType ( int position ) {

                  if( position == getItemCount() - 1 ) {

                        return 12;
                  } else {

                        return 11;
                  }
            }

            @Override
            public int getItemCount ( ) {

                  return mCategoryBeans.size() + 1;
            }

            /**
             * holder, 创建一个ConstraintLayout
             */
            class Holder extends RecyclerView.ViewHolder {

                  ConstraintLayout mConstraintLayout;

                  Holder ( View itemView ) {

                        super( itemView );
                        mConstraintLayout = itemView.findViewById( R.id.constraintLayout );
                  }

                  void bind ( int position ) {

                        if( mAdapter == null ) {
                              mAdapter = new ShowConstraintAdapter();
                        }

                        GankCategory bean = mCategoryBeans.get( position );

                        // itemView.setTag( R.id.show_item_url, bean.url );
                        //itemView.setTag( R.id.show_item_desc, bean.desc );

                        mAdapter.setCategoryBean( bean );
                        mAdapter.setConstraintLayout( mConstraintLayout );
                        mConstraintLayout.setAdapter( mAdapter );
                  }
            }

            /**
             * holder, 创建一个ConstraintLayout
             */
            class FooterHolder extends RecyclerView.ViewHolder implements OnMessageReceiveListener {

                  private boolean calledToLoadMore = false;

                  FooterHolder ( View itemView ) {

                        super( itemView );
                  }

                  void bind ( ) {

                        if( !calledToLoadMore ) {
                              calledToLoadMore = true;
                              ShowFragmentManager.getInstance().loadMore( mCategory, this );
                        }
                  }

                  @Override
                  public void onReceive ( int what, Object extra ) {

                        calledToLoadMore = false;
                        notifyItemInserted( getItemCount() - 1 );

                        Log.i( TAG, "onReceive:" + "received" );
                  }
            }
      }

      //============================ recycler adapter ============================

      //============================ recycler item adapter ============================

      /**
       * recycler item constraintLayout的adapter
       */
      private class ShowConstraintAdapter extends BaseConstraintAdapter {

            /* 设置数据 */

            private GankCategory     mCategoryBean;
            private ConstraintLayout mConstraintLayout;

            public void setCategoryBean ( GankCategory categoryBean ) {

                  mCategoryBean = categoryBean;
            }

            public void setConstraintLayout ( ConstraintLayout constraintLayout ) {

                  mConstraintLayout = constraintLayout;
            }

            @Override
            public View generateViewTo ( int i ) {

                  if( i == 0 ) {
                        TextView textView = new TextView( getContext() );
                        textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 16 );
                        textView.setTextColor( getResources().getColor( R.color.category_text ) );

                        return textView;
                  }

                  if( i <= 3 ) {
                        TextView textView = new TextView( getContext() );
                        textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 13 );
                        textView.setTextColor( getResources().getColor( R.color.category_text2 ) );
                        return textView;
                  }

                  if( i < getChildCount() ) {
                        return new ImageView( getContext() );
                  }

                  return null;
            }

            @Override
            public ConstraintLayout.LayoutParams generateLayoutParamsTo (
                int position, View view ) {

                  if( position == 0 ) {
                        return new ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                  }

                  if( position <= 3 ) {
                        return new ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                  }

                  return super.generateLayoutParamsTo( position, view );
            }

            @Override
            public Constraint generateConstraintTo ( int i, Constraint constraint, View view ) {

                  /* 介绍 */

                  if( i == 0 ) {
                        constraint.leftToLeftOfParent( 20 )
                                  .topToTopOfParent( 20 )
                                  .rightToRightOfParent( -20 );
                        return constraint;
                  }

                  /* 作者 */

                  if( i == 1 ) {

                        int imagesSize = getImagesSize();

                        if( imagesSize > 0 ) {

                              int size = constraint.getWeightWidth( 3, 1, 20 + 20 + 20 + 20 );
                              int j = imagesSize / 3;
                              int k = imagesSize % 3 == 0 ? 0 : 1;

                              constraint.leftToLeftOfParent( 20 )
                                        .topToBottomOfView( i - 1, 20 + ( j + k ) * size + 20 );
                        } else {
                              constraint.leftToLeftOfParent( 20 )
                                        .topToBottomOfView( i - 1, 20 );
                        }
                        return constraint;
                  } else if( i == 2 ) {

                        /* 日期 */

                        constraint.rightToRightOfParent( -20 )
                                  .topToTopOfView( i - 1, 0 );
                        return constraint;
                  } else if( i == 3 ) {

                        /* 分类 */

                        constraint.rightToLeftOfView( i - 1, -20 )
                                  .topToTopOfView( i - 1, 0 );

                        int count = mConstraintLayout.getChildCount();

                        for( int j = 4; j < count; j++ ) {

                              View child = mConstraintLayout.getChildAt( j );
                              /* 隐藏imageView,设置bitmap引用为null,释放bitmap */
                              child.setVisibility( View.GONE );
                              ( (ImageView) child ).setImageBitmap( null );
                        }

                        return constraint;
                  }

                  /* 如果有图片数据的话,会走到这里 */

                  int j = i - 4;
                  if( j == 0 ) {

                        /* 以第一张图片为基础,从左到右,从上到下排列 */

                        int size = constraint.getWeightWidth( 3, 1, 20 + 20 + 20 + 20 );
                        constraint.leftToLeftOfParent( 20, size ).topToBottomOfView( 0, 20, size );
                  } else if( j <= 2 ) {

                        constraint.copyFrom( i - 1 )
                                  .translateX( constraint.getViewWidth( i - 1 ) + 20 );
                  } else {

                        constraint.copyFrom( i - 3 )
                                  .translateY( constraint.getViewHeight( i - 3 ) + 20 );
                  }

                  /* 因为recycler item 不断复用,前面没有数据的话会将图片隐藏,现在有图片数据,需要还原为可见 */

                  CharSequence text = ( (TextView) mConstraintLayout.getChildAt( 0 ) ).getText();
                  Log.i(
                      TAG,
                      "generateConstraintTo:" + i + constraint + " " + view.getVisibility() + " "
                          + text
                  );
                  view.setVisibility( View.VISIBLE );

                  /* 加载图片数据 */

                  //String url = mCategoryBean.images.get( j );
                  ImageView imageView = (ImageView) view;
                  imageView.setImageResource( R.drawable.music );
                  //imageView.setTag( R.id.show_item_image, url );

                  return constraint;
            }

            @Override
            public int getChildCount ( ) {

                  return 4 + getImagesSize();
            }

            @Override
            public void beforeMeasure ( int position, View view ) {

                  /* 设置数据 */

                  if( position == 0 ) {
                        if( mCategoryBean != null ) {
                              // ( (TextView) view ).setText( mCategoryBean.desc );
                        }
                  }

                  if( mCategoryBean != null ) {

                        switch( position ) {
                              case 1:
                                    //   ( (TextView) view ).setText( mCategoryBean.who );
                                    break;
                              case 2:
                                    //( (TextView) view )
                                    //    .setText( mCategoryBean.publishedAt.substring( 0, 10 ) );
                                    break;
                              case 3:
                                    //( (TextView) view ).setText( mCategoryBean.type );
                                    break;
                              default:
                                    break;
                        }
                  }
            }

            public int getImagesSize ( ) {

                  return 0;//mCategoryBean.images == null ? 0 : mCategoryBean.images.size();
            }
      }

      //============================ recycler item click ============================

      /**
       * recycler normal item click
       */
      private class ShowItemClickListener implements View.OnClickListener {

            @Override
            public void onClick ( View v ) {

                  String url = (String) v.getTag( R.id.item_url );
                  String title = (String) v.getTag( R.id.show_item_desc );
                  WebActivity.start( getContext(), url, title );
            }
      }
}
