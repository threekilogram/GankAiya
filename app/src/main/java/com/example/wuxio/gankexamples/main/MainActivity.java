package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.banner.BannerView;
import com.example.banner.adapter.BasePagerAdapter;
import com.example.system_ui.SystemUI;
import com.example.viewskin.ContainerLayout;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.RootActivity;
import com.example.wuxio.gankexamples.main.fragment.ShowFragment;
import com.example.wuxio.gankexamples.picture.PictureActivity;
import com.example.wuxio.gankexamples.utils.BackPressUtil;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;
import com.example.wuxio.gankexamples.utils.image.RoundBitmapFactory;

import java.io.File;
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
    protected ContainerLayout         mBannerContainer;
    protected CollapsingToolbarLayout mCollapsingToolbar;
    private   BannerAdapter           mBannerAdapter;


    /**
     * 静态启动方法
     */
    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        //MainManager.getInstance().register(this);

        initView();
        setSystemUI();
        postAction();
    }


    /**
     * 初始化view
     */
    private void initView() {

        mDrawer = findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.navigationView);
        mBanner = findViewById(R.id.banner);
        mAppBar = findViewById(R.id.appBar);
        mCoordinator = findViewById(R.id.coordinator);
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mCollapsingToolbar = findViewById(R.id.collapsingToolbar);

        /* 防止tabLayout 进入statusBar */
        int height = SystemUI.getStatusBarHeight(MainActivity.this);
        mCollapsingToolbar.setMinimumHeight(height);

        /* banner */
        mBannerAdapter = new BannerAdapter();
        mBanner.setAdapter(mBannerAdapter);

        /* view Pager */
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }


    /**
     * 创建好 activity 之后执行一些初始化activity的操作
     */
    private void postAction() {

        mDrawer.post(() -> {

            /* 设置导航菜单 */
            initNavigationView(mNavigationView);

            /* 为activity执行后台初始化操作 */
            //MainManager.getInstance().onActivityCreate();
        });
    }


    /**
     * 设置状态栏
     */
    private void setSystemUI() {

        SystemUI.setStatusColor(this, Color.TRANSPARENT);
    }


    /**
     * 关闭菜单,{@link NavigationItemClickListener#onClick(View)}
     */
    private void closeDrawer() {

        mDrawer.closeDrawer(Gravity.START);
    }

    //============================ 设置导航栏界面 ============================


    /**
     * 设置navigation布局,因为需要获得view宽高,使用post runnable 读取
     *
     * @param navigationView 导航view
     */
    private void initNavigationView(NavigationView navigationView) {

        View headerView = navigationView.getHeaderView(0);
        ImageView avatarImageView = headerView.findViewById(R.id.userAvatar);

        /* 设置圆角图片给导航栏的头像框 */

        Bitmap bitmap = BitmapReader.decodeSampledBitmap(
                getResources(),
                R.drawable.avatar,
                avatarImageView.getWidth(),
                avatarImageView.getHeight());

        Drawable drawable = RoundBitmapFactory.circleBitmap(this, bitmap);
        avatarImageView.setImageDrawable(drawable);

        /* 给导航栏条目设置点击事件 */

        NavigationItemClickListener clickListener = new NavigationItemClickListener();
        headerView.findViewById(R.id.toAbout).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toQuestFeedback).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toDonate).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toLoginGithub).setOnClickListener(clickListener);
        headerView.findViewById(R.id.exitApp).setOnClickListener(clickListener);
    }

    //============================ 导航栏功能 ============================

    /**
     * 导航栏item点击事件
     */
    private class NavigationItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

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
        private void toAbout() {

            Log.i(TAG, "toAbout:" + "");

        }


        /**
         * to QuestionFeedback page
         */
        private void toQuestionFeedback() {

            Log.i(TAG, "toQuestionFeedback:" + "");

        }


        /**
         * to Donate page
         */
        private void toDonate() {

            Log.i(TAG, "toDonate:" + "");

        }


        /**
         * to LoginGitHub page
         */
        private void toLoginGitHub() {

            Log.i(TAG, "toLoginGitHub:" + "");

        }


        /**
         * to exitApp
         */
        private void exitApp() {

            Log.i(TAG, "exitApp:" + "");
            RootActivity.start(MainActivity.this);
        }
    }

    //============================ 返回按键 ============================


    /**
     * 添加点击两次退出activity
     */
    @Override
    public void onBackPressed() {

        if (BackPressUtil.showInfo(this)) {
            RootActivity.start(this);
            super.onBackPressed();
        }
    }

    //============================ banner adapter ============================


    /**
     * 用于与{@link MainManager}交互,准备好banner的图片之后,通知activity设置给banner
     *
     * @param urls        图片地址
     * @param bitmapFiles 图片文件
     */
    public void setBannerImageData(
            List< String > urls,
            ArrayMap< String, File > bitmapFiles,
            List< Bitmap > bitmaps) {

        mBannerAdapter.setBitmapFiles(urls, bitmapFiles, bitmaps);
    }


    public BannerView getBanner() {

        return mBanner;
    }


    /**
     * banner adapter
     */
    private class BannerAdapter extends BasePagerAdapter< Bitmap, ImageView > {


        /**
         * 正在显示的图片
         */
        private List< Bitmap >           mBitmaps;
        /**
         * 图片文件
         */
        private ArrayMap< String, File > mBitmapFileMap;
        /**
         * 图片对应url
         */
        private List< String >           mUrls;
        /**
         * 每个item点击事件
         */
        private BannerItemClickListener  mBannerItemClickListener;
        /**
         * 默认大小
         */
        private static final int DEFAULT_COUNT = 5;


        /**
         * 更新数据
         */
        public void setBitmapFiles(
                List< String > urls,
                ArrayMap< String, File > bitmapFileMap,
                List< Bitmap > bitmaps) {

            mUrls = urls;
            mBitmapFileMap = bitmapFileMap;
            mBitmaps = bitmaps;

        }


        @Override
        public int getCount() {

            return DEFAULT_COUNT;
        }


        @Override
        public Bitmap getData(int i) {

            if (mBitmaps != null) {
                Bitmap bitmap = mBitmaps.get(i);
                if (bitmap != null) {
                    return bitmap;
                }
            }
            return null;
        }


        @Override
        public ImageView getView(int i) {

            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }


        @Override
        public void bindData(int i, Bitmap o, ImageView imageView) {

            if (o != null) {
                imageView.setImageBitmap(o);
                if (mBannerItemClickListener == null) {
                    mBannerItemClickListener = new BannerItemClickListener();
                }
                imageView.setTag(R.id.main_banner_item_tag, i);
                imageView.setOnClickListener(mBannerItemClickListener);
            }
        }


        /**
         * banner item 点击事件,跳转到{@link PictureActivity}
         */
        private class BannerItemClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag(R.id.main_banner_item_tag);
                PictureActivity.start(MainActivity.this, position, mUrls, mBitmapFileMap);

                // TODO: 2018-05-07 转场动画 ,更新数据
            }
        }
    }

    //============================ pager adapter ============================

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {

            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            return ShowFragment.newInstance();
        }


        @Override
        public int getCount() {

            return 5;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return "pagerTitle";
        }
    }
}