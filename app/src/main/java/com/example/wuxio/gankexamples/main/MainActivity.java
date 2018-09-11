package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.constant.Constant;
import com.example.wuxio.gankexamples.main.fragment.ShowFragment;
import com.example.wuxio.gankexamples.picture.PictureActivity;
import com.example.wuxio.gankexamples.root.RootActivity;
import com.example.wuxio.gankexamples.utils.BackPressUtil;
import com.threekilogram.banner.BannerView;
import com.threekilogram.banner.adapter.BasePagerAdapter;
import com.threekilogram.bitmapreader.BitmapReader;
import com.threekilogram.bitmapreader.RoundBitmapFactory;
import com.threekilogram.drawable.anim.BiliBiliLoadingDrawable;
import com.threekilogram.systemui.SystemUi;
import java.util.List;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

      private static final String TAG = "MainActivity";

      protected DrawerLayout            mDrawer;
      protected NavigationView          mNavigationView;
      protected BannerView              mBanner;
      protected AppBarLayout            mAppBar;
      protected CoordinatorLayout       mCoordinator;
      protected ViewPager               mViewPager;
      protected TabLayout               mTabLayout;
      protected CollapsingToolbarLayout mCollapsingToolbar;
      protected ImageView               mBannerLoading;
      private   BannerAdapter           mBannerAdapter;
      private   MainPagerAdapter        mMainPagerAdapter;
      private   MainPagerChangeListener mMainPagerChangeListener;
      private   BiliBiliLoadingDrawable mBiliLoadingDrawable;

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

            initView();
            setSystemUI();
            postAction();

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

            /* banner Loading */
            initBannerLoading();

            /* 防止tabLayout 进入statusBar */
            int height = SystemUi.getStatusBarHeight( MainActivity.this );
            mCollapsingToolbar.setMinimumHeight( height );

            /* banner */
            mBannerAdapter = new BannerAdapter();
            mBanner.setAdapter( mBannerAdapter );

            /* view Pager */
            mMainPagerAdapter = new MainPagerAdapter( getSupportFragmentManager() );
            mViewPager.setAdapter( mMainPagerAdapter );

            /* tabLayout */
            mMainPagerChangeListener = new MainPagerChangeListener();
            mViewPager.addOnPageChangeListener( mMainPagerChangeListener );
            mTabLayout.setupWithViewPager( mViewPager );
      }

      /**
       * 创建好 activity 之后执行一些初始化activity的操作
       */
      private void postAction ( ) {

            mDrawer.post( ( ) -> {

                  mBanner.stopLoop();

                  /* 设置导航菜单 */
                  initNavigationView( mNavigationView );

                  /* 设置默认页 */
                  mViewPager.setCurrentItem( 0 );

                  mMainPagerChangeListener.onPageSelected( 0 );
            } );
      }

      /**
       * 设置状态栏
       */
      private void setSystemUI ( ) {

            SystemUi.transparentStatus( MainActivity.this );
      }

      /**
       * 关闭菜单,{@link NavigationItemClickListener#onClick(View)}
       */
      private void closeDrawer ( ) {

            mDrawer.closeDrawer( Gravity.START );
      }

      /**
       * 初始化banner Loading
       */
      private void initBannerLoading ( ) {

            mBiliLoadingDrawable = new BiliBiliLoadingDrawable(
                getResources().getDimensionPixelSize( R.dimen.banner_loading_size ) );
            mBiliLoadingDrawable.setStrokeWidth( 5 );
            mBiliLoadingDrawable.setDuration( 2400 );
            mBiliLoadingDrawable.setRepeat( 300000 );
            mBiliLoadingDrawable.setRadius( 7 );
            mBiliLoadingDrawable.setPaintColor( getResources().getColor( R.color.blue ) );
            mBannerLoading.setImageDrawable( mBiliLoadingDrawable );
            mBiliLoadingDrawable.start();
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

      public void notifyWithoutBannerResource ( ) {

            Toast.makeText( this, "无法收到电波", Toast.LENGTH_SHORT ).show();
      }

      //============================ 设置导航栏界面 ============================

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
                avatarImageView.getWidth(),
                avatarImageView.getHeight()
            );

            Drawable drawable = RoundBitmapFactory.circleBitmap( this, bitmap );
            avatarImageView.setImageDrawable( drawable );

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
      private class NavigationItemClickListener implements View.OnClickListener {

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

      //============================ 返回按键 ============================

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

      //============================ banner adapter ============================

      public BannerView getBanner ( ) {

            return mBanner;
      }

      /**
       * 轮播图适配器
       */
      private class BannerAdapter extends BasePagerAdapter<Bitmap, ImageView> {

            private int          mDataStartIndex;
            private List<Bitmap> mBitmaps;

            /**
             * 每个item点击事件
             */
            private BannerItemClickListener mBannerItemClickListener;

            @Override
            public int getCount ( ) {

                  return 5;
            }

            @Override
            public Bitmap getData ( int i ) {

                  try {
                        Bitmap bitmap = mBitmaps.get( i );
                        if( bitmap != null ) {
                              return bitmap;
                        }
                  } catch(Exception e) {
                        Log.e( "MainActivity", "nothing to worry about" );
                  }
                  return null;
            }

            @Override
            public ImageView getView ( ViewGroup container, int position ) {

                  ImageView imageView = new ImageView( MainActivity.this );
                  imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
                  return imageView;
            }

            @Override
            public void bindData ( int i, Bitmap o, ImageView imageView ) {

                  if( o != null ) {
                        imageView.setImageBitmap( o );
                        if( mBannerItemClickListener == null ) {
                              mBannerItemClickListener = new BannerItemClickListener();
                        }
                        imageView.setTag( R.id.main_banner_item_tag, i );
                        imageView.setOnClickListener( mBannerItemClickListener );
                  }
            }

            public void reBindData ( int position ) {

                  ImageView view = getItemView( position );
                  Bitmap bitmap = getData( position );
                  view.setImageBitmap( bitmap );
            }

            /**
             * banner item 点击事件,跳转到{@link PictureActivity}
             */
            private class BannerItemClickListener implements View.OnClickListener {

                  @Override
                  public void onClick ( View v ) {

                        int position = (Integer) v.getTag( R.id.main_banner_item_tag );
                        PictureActivity.start( MainActivity.this, mDataStartIndex, position );
                  }
            }
      }

      //============================ pager adapter ============================

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

      //============================ pager scroll ============================

      private class MainPagerChangeListener extends ViewPager.SimpleOnPageChangeListener {

            @Override
            public void onPageSelected ( int position ) {

                  ShowFragment fragment = mMainPagerAdapter.getCurrentFragment( position );
                  String category = Constant.All_CATEGORY[ position ];
                  fragment.loadData( category );
            }
      }
}