package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.constant.Constant;
import com.example.wuxio.gankexamples.main.fragment.ShowFragment;
import com.example.wuxio.gankexamples.root.RootActivity;
import com.example.wuxio.gankexamples.utils.BackPressUtil;
import com.threekilogram.bitmapreader.BitmapReader;
import com.threekilogram.drawable.anim.BiliBiliLoadingDrawable;
import com.threekilogram.systemui.SystemUi;
import tech.threekilogram.pager.banner.RecyclerPagerBanner;
import tech.threekilogram.pager.banner.RecyclerPagerBanner.BannerAdapter;
import tech.threekilogram.pager.indicator.DotView;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScroll;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

      private static final String TAG = "MainActivity";

      protected DrawerLayout            mDrawer;
      protected NavigationView          mNavigationView;
      protected RecyclerPagerBanner     mBanner;
      private   RecyclerBannerAdapter   mAdapter;
      protected AppBarLayout            mAppBar;
      protected CoordinatorLayout       mCoordinator;
      protected ViewPager               mViewPager;
      protected TabLayout               mTabLayout;
      protected CollapsingToolbarLayout mCollapsingToolbar;
      protected ImageView               mBannerLoading;
      private   MainPagerAdapter        mMainPagerAdapter;
      private   MainPagerChangeListener mMainPagerChangeListener;
      private   BiliBiliLoadingDrawable mBiliLoadingDrawable;
      private   DotView                 mDotView;

      /**
       * 静态启动方法
       */
      public static void start ( Context context ) {

            Intent starter = new Intent( context, MainActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_main );

            setSystemUI();
            initView();
            postAction();

            MainModel.bind( this );
            MainModel.loadBannerBitmap( this );
      }

      /**
       * 设置状态栏
       */
      private void setSystemUI ( ) {

            SystemUi.transparentStatus( MainActivity.this );
      }

      /**
       * 初始化view
       */
      private void initView ( ) {

            mDrawer = findViewById( R.id.drawer );
            mNavigationView = findViewById( R.id.navigationView );
            mBanner = findViewById( R.id.banner );
            mAppBar = findViewById( R.id.appBar );
            mCoordinator = findViewById( R.id.coordinator );
            mViewPager = findViewById( R.id.viewPager );
            mTabLayout = findViewById( R.id.tabLayout );
            mCollapsingToolbar = findViewById( R.id.collapsingToolbar );
            mBannerLoading = findViewById( R.id.bannerLoading );

            /* 设置导航菜单 */
            initNavigationView( mNavigationView );

            /* banner Loading */
            initBannerLoading();

            /* banner indicator */
            mDotView = findViewById( R.id.dotView );
            mDotView.setDotCount( 5 );
            mDotView.setSelected( 0 );

            /* banner */
            mAdapter = new RecyclerBannerAdapter();
            mBanner.setBannerAdapter( mAdapter );
            RecyclerPagerScroll scroll = new RecyclerPagerScroll( mBanner.getRecyclerPager() );
            scroll.setOnRecyclerPagerScrollListener( new BannerScrollListener() );

            /* 防止tabLayout 进入statusBar */
            int height = SystemUi.getStatusBarHeight( MainActivity.this );
            mCollapsingToolbar.setMinimumHeight( height );

            /* view Pager */
            mMainPagerAdapter = new MainPagerAdapter( getSupportFragmentManager() );
            mViewPager.setAdapter( mMainPagerAdapter );

            /* tabLayout */
            mMainPagerChangeListener = new MainPagerChangeListener();
            mViewPager.addOnPageChangeListener( mMainPagerChangeListener );
            mTabLayout.setupWithViewPager( mViewPager );
      }

      /**
       * 初始化banner Loading
       */
      private void initBannerLoading ( ) {

            mBiliLoadingDrawable = new BiliBiliLoadingDrawable(
                getResources().getDimensionPixelSize( R.dimen.banner_loading_size ) );
            mBiliLoadingDrawable.setStrokeWidth( 5 );
            mBiliLoadingDrawable.setDuration( 2400 );
            mBiliLoadingDrawable.setRepeat( Integer.MAX_VALUE / 2400 );
            mBiliLoadingDrawable.setRadius( 7 );
            mBiliLoadingDrawable.setPaintColor( getResources().getColor( R.color.blue ) );
            mBannerLoading.setImageDrawable( mBiliLoadingDrawable );
            mBiliLoadingDrawable.start();
      }

      /**
       * 创建好 activity 之后执行一些初始化activity的操作
       */
      private void postAction ( ) {

            mDrawer.post( ( ) -> {

                  /* 设置默认页 */
                  mViewPager.setCurrentItem( 0 );
                  mMainPagerChangeListener.onPageSelected( 0 );
            } );
      }

      /**
       * 关闭菜单,{@link NavigationItemClickListener#onClick(View)}
       */
      private void closeDrawer ( ) {

            mDrawer.closeDrawer( Gravity.START );
      }

      /**
       * 显示loading
       */
      private void showBannerLoading ( ) {

            mBannerLoading.setVisibility( View.VISIBLE );
            mBiliLoadingDrawable.start();
      }

      /**
       * 隐藏loading
       */
      private void hideBannerLoading ( ) {

            mBannerLoading.setVisibility( View.INVISIBLE );
            mBiliLoadingDrawable.stop();
      }

      /**
       * 设置navigation布局,因为需要获得view宽高,使用post runnable 读取
       *
       * @param navigationView 导航view
       */
      private void initNavigationView ( NavigationView navigationView ) {

            View headerView = navigationView.getHeaderView( 0 );
            ImageView avatarImageView = headerView.findViewById( R.id.userAvatar );

            /* 设置圆角图片给导航栏的头像框 */

            Bitmap bitmap = BitmapReader.sampledBitmap(
                this,
                R.drawable.avatar,
                ScreenSize.resToPx( this, R.dimen.user_avatar_size ),
                ScreenSize.resToPx( this, R.dimen.user_avatar_size )
            );
            avatarImageView.setImageBitmap( bitmap );

            /* 给导航栏条目设置点击事件 */

            NavigationItemClickListener clickListener = new NavigationItemClickListener();
            headerView.findViewById( R.id.toAbout ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toQuestFeedback ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toDonate ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toLoginGithub ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.exitApp ).setOnClickListener( clickListener );
      }

      //============================ 导航栏功能 ============================

      /**
       * 导航栏item点击事件
       */
      private class NavigationItemClickListener implements OnClickListener {

            @Override
            public void onClick ( View v ) {

                  switch( v.getId() ) {

                        case R.id.toAbout:
                              toAbout();
                              break;

                        case R.id.toQuestFeedback:
                              toQuestionFeedback();
                              break;

                        case R.id.toDonate:
                              toDonate();
                              break;

                        case R.id.toLoginGithub:
                              toLoginGitHub();
                              break;

                        case R.id.exitApp:
                              exitApp();
                              break;

                        default:
                              break;
                  }
                  closeDrawer();
            }

            /**
             * to about page
             */
            private void toAbout ( ) {

                  Log.i( TAG, "toAbout:" + "" );
            }

            /**
             * to QuestionFeedback page
             */
            private void toQuestionFeedback ( ) {

                  Log.i( TAG, "toQuestionFeedback:" + "" );
            }

            /**
             * to Donate page
             */
            private void toDonate ( ) {

                  Log.i( TAG, "toDonate:" + "" );
            }

            /**
             * to LoginGitHub page
             */
            private void toLoginGitHub ( ) {

                  Log.i( TAG, "toLoginGitHub:" + "" );
            }

            /**
             * to exitApp
             */
            private void exitApp ( ) {

                  Log.i( TAG, "exitApp:" + "" );
                  RootActivity.start( MainActivity.this );
            }
      }

      /**
       * 添加点击两次退出activity
       */
      @Override
      public void onBackPressed ( ) {

            if( BackPressUtil.showInfo( this ) ) {
                  RootActivity.start( this );
                  super.onBackPressed();
            }
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      /**
       * banner adapter
       */
      private class RecyclerBannerAdapter extends BannerAdapter<BannerHolder> {

            @Override
            public int getActualCount ( ) {

                  return 5;
            }

            @Override
            public int getActualPosition ( int position ) {

                  return super.getActualPosition( position );
            }

            @NonNull
            @Override
            public BannerHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  View view = LayoutInflater.from( parent.getContext() )
                                            .inflate( R.layout.main_banner_item, parent, false );
                  return new BannerHolder( view );
            }

            @Override
            public void onBindViewHolder ( @NonNull BannerHolder holder, int position ) {

                  int actualPosition = getActualPosition( position );
            }
      }

      /**
       * banner item view holder
       */
      private class BannerHolder extends ViewHolder {

            public BannerHolder ( View itemView ) {

                  super( itemView );
            }
      }

      /**
       * main content fragment adapter
       */
      private class MainPagerAdapter extends FragmentStatePagerAdapter {

            private ShowFragment[] mShowFragments = new ShowFragment[ Constant.All_CATEGORY.length ];

            MainPagerAdapter ( FragmentManager fm ) {

                  super( fm );
            }

            @Override
            public Fragment getItem ( int position ) {

                  ShowFragment fragment = ShowFragment.newInstance();
                  mShowFragments[ position ] = fragment;
                  return fragment;
            }

            public ShowFragment getCurrentFragment ( int position ) {

                  return mShowFragments[ position ];
            }

            @Override
            public int getCount ( ) {

                  return Constant.All_CATEGORY.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle ( int position ) {

                  return Constant.All_CATEGORY[ position ];
            }

            @Override
            public void destroyItem ( ViewGroup container, int position, Object object ) {

                  super.destroyItem( container, position, object );
            }
      }

      private class MainPagerChangeListener extends SimpleOnPageChangeListener {

            @Override
            public void onPageSelected ( int position ) {

//                  ShowFragment fragment = mMainPagerAdapter.getCurrentFragment( position );
//                  String category = Constant.All_CATEGORY[ position ];
//                  fragment.loadData( category );
            }
      }

      private class BannerScrollListener implements OnRecyclerPagerScrollListener {

            @Override
            public void onScroll (
                int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) {

                  mDotView.setSelected( mAdapter.getActualPosition( currentPosition ) );
            }

            @Override
            public void onPageSelected ( int prevSelected, int newSelected ) {

            }
      }
}