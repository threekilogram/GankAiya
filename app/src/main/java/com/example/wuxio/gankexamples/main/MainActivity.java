package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.ImageView.ScaleType;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.about.AboutAppActivity;
import com.example.wuxio.gankexamples.main.fragment.ShowFragment;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.picture.PictureActivity;
import com.example.wuxio.gankexamples.root.RootActivity;
import com.example.wuxio.gankexamples.splash.SplashActivity;
import com.example.wuxio.gankexamples.utils.BackPressUtil;
import com.example.wuxio.gankexamples.web.WebActivity;
import com.threekilogram.drawable.BiliBiliLoadingDrawable;
import com.threekilogram.drawable.widget.StaticAnimateDrawableView;
import com.threekilogram.systemui.SystemUi;
import java.util.List;
import tech.threekilogram.executor.MainExecutor;
import tech.threekilogram.pager.banner.RecyclerPagerBanner;
import tech.threekilogram.pager.banner.RecyclerPagerBanner.BannerAdapter;
import tech.threekilogram.pager.indicator.DotView;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScrollListener;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

      private static final String TAG = MainActivity.class.getName();

      private View                      mRoot;
      private DrawerLayout              mDrawer;
      private NavigationView            mNavigationView;
      private RecyclerPagerBanner       mBanner;
      private RecyclerBannerAdapter     mBannerAdapter;
      private AppBarLayout              mAppBar;
      private CoordinatorLayout         mCoordinator;
      private ViewPager                 mViewPager;
      private TabLayout                 mTabLayout;
      private CollapsingToolbarLayout   mCollapsingToolbar;
      private StaticAnimateDrawableView mBannerLoading;
      private MainPagerAdapter          mPagerAdapter;
      private DotView                   mDotView;
      private MainTabSelectListener     mTabSelectListener;
      private MainOnPageChangedListener mPageChangedListener;

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

            /* singleTask 模式 */
            /* 先启动splash页面,该逻辑用于优化mainActivity预加载,当从splash返回时,MainActivity已经准备好页面,防止卡顿 */
            SplashActivity.start( this );
            /* 开始预加载页面 */
            MainExecutor.execute(
                ( ) -> {
                      mRoot = LayoutInflater.from( MainActivity.this )
                                            .inflate( R.layout.activity_main, null );
                }
            );
      }

      @Override
      protected void onNewIntent ( Intent intent ) {

            super.onNewIntent( intent );

            /* 从splash 跳过来了,设置界面 */
            if( StaticAnimateDrawableView.getDrawable() == null ) {
                  BiliBiliLoadingDrawable drawable = createBiliBiliLoadingDrawable( this );
                  StaticAnimateDrawableView.setDrawable( drawable );
                  StaticAnimateDrawableView.setDuration( 3000 );
            }

            setSystemUI();
            super.setContentView( mRoot );

            initView();
            BeautyModel.bind( this );
            BeautyModel.loadBannerBitmap();
            postAction( mDrawer );
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

            /* banner indicator */
            mDotView = findViewById( R.id.dotView );
            mDotView.setDotCount( 5 );
            mDotView.setSelected( 0 );
            mDotView.setColorSelected( getResources().getColor( R.color.blue ) );

            /* banner */
            mBannerAdapter = new RecyclerBannerAdapter();
            mBanner.setBannerAdapter( mBannerAdapter );
            mBanner.addOnScrollListener( new RecyclerPagerScrollListener() {

                  @Override
                  protected void onPageSelected ( int currentPosition, int nextPosition ) {

                        super.onPageSelected( currentPosition, nextPosition );
                        int actualPosition = mBannerAdapter.getActualPosition( nextPosition );
                        mDotView.setSelected( actualPosition );
                  }
            } );

            /* 防止tabLayout 进入statusBar */
            int height = SystemUi.getStatusBarHeight( MainActivity.this );
            mCollapsingToolbar.setMinimumHeight( height );

            /* view Pager */
            mPagerAdapter = new MainPagerAdapter( getSupportFragmentManager() );
            mViewPager.setAdapter( mPagerAdapter );
            mPageChangedListener = new MainOnPageChangedListener();
            mViewPager.addOnPageChangeListener( mPageChangedListener );

            /* tabLayout */
            mTabLayout.setupWithViewPager( mViewPager );
            mTabSelectListener = new MainTabSelectListener();
            mTabLayout.addOnTabSelectedListener( mTabSelectListener );
      }

      @NonNull
      public static BiliBiliLoadingDrawable createBiliBiliLoadingDrawable ( Context context ) {

            Resources resources = context.getResources();
            BiliBiliLoadingDrawable loadingDrawable = new BiliBiliLoadingDrawable(
                resources.getDimensionPixelSize( R.dimen.banner_loading_size ) );
            loadingDrawable.setColor( resources.getColor( R.color.blue ) );
            loadingDrawable.setStrokeWidth( 5 );
            loadingDrawable.setRadius( 8 );
            return loadingDrawable;
      }

      /**
       * 创建好 activity 之后执行一些初始化activity的操作
       */
      private void postAction ( View view ) {

            view.post( ( ) -> {

                  /* 设置默认页 */
                  mViewPager.setCurrentItem( 0 );
                  mTabSelectListener.onTabSelected( mTabLayout.getTabAt( 0 ) );
                  mPageChangedListener.onPageSelected( 0 );
                  mPageChangedListener.onPageScrollStateChanged( ViewPager.SCROLL_STATE_IDLE );
            } );
      }

      /**
       * 添加点击两次退出activity
       */
      @Override
      public void onBackPressed ( ) {

            if( BackPressUtil.showInfo( this ) ) {
                  RootActivity.quitApp( this );
                  super.onBackPressed();
            }
      }

      @Override
      protected void onDestroy ( ) {

            StaticAnimateDrawableView.clearView();
            super.onDestroy();
      }

      /**
       * 设置navigation布局,因为需要获得view宽高,使用post runnable 读取
       *
       * @param navigationView 导航view
       */
      private void initNavigationView ( NavigationView navigationView ) {

            View headerView = navigationView.getHeaderView( 0 );
            ImageView avatarImageView = headerView.findViewById( R.id.userAvatar );

            /* 给导航栏条目设置点击事件 */

            NavigationItemClickListener clickListener = new NavigationItemClickListener();
            headerView.findViewById( R.id.toAbout ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toQuestFeedback ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toDonate ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.toLoginGithub ).setOnClickListener( clickListener );
            headerView.findViewById( R.id.exitApp ).setOnClickListener( clickListener );
      }

      public void onBannerBitmapsPrepared ( int startIndex, List<Bitmap> bitmaps ) {

            mBannerAdapter.mStartIndex = startIndex;
            mBannerAdapter.mBitmaps = bitmaps;
            mBannerAdapter.notifyDataSetChanged();

            hideBannerLoading();
      }

      /**
       * 关闭菜单,{@link NavigationItemClickListener#onClick(View)}
       */
      private void closeDrawer ( ) {

            mDrawer.closeDrawer( Gravity.START );
      }

      private void startLoop ( ) {

            mBanner.startLoop( 4000 );
      }

      private void stopLoop ( ) {

            mBanner.stopLoop();
      }

      @Override
      protected void onRestart ( ) {

            super.onRestart();
      }

      /**
       * 显示loading
       */
      private void showBannerLoading ( ) {

            mBannerLoading.setVisibility( View.VISIBLE );
      }

      /**
       * 隐藏loading
       */
      private void hideBannerLoading ( ) {

            mBannerLoading.setVisibility( View.INVISIBLE );
      }

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

                  AboutAppActivity.start( MainActivity.this );
            }

            /**
             * to QuestionFeedback page
             */
            private void toQuestionFeedback ( ) {

            }

            /**
             * to Donate page
             */
            private void toDonate ( ) {

            }

            /**
             * to LoginGitHub page
             */
            private void toLoginGitHub ( ) {

                  WebActivity
                      .start( MainActivity.this, "https://github.com/threekilogram", "Github" );
            }

            /**
             * to exitApp
             */
            private void exitApp ( ) {

                  RootActivity.quitApp( MainActivity.this );
            }
      }

      /**
       * banner adapter
       */
      private class RecyclerBannerAdapter extends BannerAdapter<BannerHolder> {

            private List<Bitmap>            mBitmaps;
            private int                     mStartIndex;
            private BannerItemClickListener mClickListener = new BannerItemClickListener();

            @Override
            public int getActualCount ( ) {

                  return 5;
            }

            @NonNull
            @Override
            public BannerHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  ImageView imageView = new ImageView( parent.getContext() );
                  imageView.setScaleType( ScaleType.CENTER_CROP );
                  BannerHolder bannerHolder = new BannerHolder( imageView );
                  bannerHolder.setClickListener( mClickListener );
                  return bannerHolder;
            }

            @Override
            public void onBindViewHolder ( @NonNull BannerHolder holder, int position ) {

                  if( mBitmaps != null ) {
                        int actualPosition = getActualPosition( position );
                        Bitmap bitmap = mBitmaps.get( actualPosition );
                        holder.bind( actualPosition, bitmap );
                  }
            }
      }

      /**
       * banner item view holder
       */
      private class BannerHolder extends ViewHolder {

            private int mCurrentPosition;

            private BannerHolder ( View itemView ) {

                  super( itemView );
            }

            private void bind ( int position, Bitmap bitmap ) {

                  mCurrentPosition = position;
                  ( (ImageView) itemView ).setImageBitmap( bitmap );
            }

            private void setClickListener ( BannerItemClickListener listener ) {

                  itemView.setOnClickListener( listener );
            }
      }

      /**
       * banner item click
       */
      private class BannerItemClickListener implements OnClickListener {

            @Override
            public void onClick ( View v ) {

                  if( mBannerAdapter.mBitmaps != null ) {

                        BannerHolder childViewHolder = (BannerHolder) mBanner.getRecyclerPager()
                                                                             .getChildViewHolder(
                                                                                 v );

                        Log.e( TAG, "onClick : " + childViewHolder.mCurrentPosition );

                        PictureActivity.start(
                            MainActivity.this,
                            mBannerAdapter.mStartIndex,
                            childViewHolder.mCurrentPosition,
                            mBannerAdapter.mBitmaps
                        );
                  }
            }
      }

      /**
       * main content fragment adapter
       */
      private class MainPagerAdapter extends FragmentPagerAdapter {

            private ShowFragment[] mShowFragments = new ShowFragment[ GankUrl.CATEGORY.length ];

            {
                  for( int i = 0; i < GankUrl.CATEGORY.length; i++ ) {
                        mShowFragments[ i ] = ShowFragment.newInstance( GankUrl.CATEGORY[ i ] );
                  }
            }

            MainPagerAdapter ( FragmentManager fm ) {

                  super( fm );
            }

            @Override
            public Fragment getItem ( int position ) {

                  return mShowFragments[ position ];
            }

            public ShowFragment getCurrentFragment ( int position ) {

                  return mShowFragments[ position ];
            }

            @Override
            public int getCount ( ) {

                  return GankUrl.CATEGORY.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle ( int position ) {

                  return GankUrl.CATEGORY[ position ];
            }
      }

      /**
       * pager selected
       */
      private class MainTabSelectListener implements OnTabSelectedListener {

            @Override
            public void onTabSelected ( Tab tab ) {

                  mPagerAdapter.mShowFragments[ tab.getPosition() ].onSelected();
            }

            @Override
            public void onTabUnselected ( Tab tab ) {

                  mPagerAdapter.mShowFragments[ tab.getPosition() ].onUnSelected();
            }

            @Override
            public void onTabReselected ( Tab tab ) {

                  mPagerAdapter.mShowFragments[ tab.getPosition() ].onReselected();
            }
      }

      private class MainOnPageChangedListener implements OnPageChangeListener {

            private int mPosition;

            @Override
            public void onPageScrolled (
                int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageSelected ( int position ) {

                  mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged ( int state ) {

                  if( state == ViewPager.SCROLL_STATE_IDLE ) {
                        mPagerAdapter.mShowFragments[ mPosition ].onIdle();
                  }
            }
      }
}