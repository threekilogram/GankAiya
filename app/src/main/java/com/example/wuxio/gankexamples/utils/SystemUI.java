package com.example.wuxio.gankexamples.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @author wuxio 2018-04-22:17:51
 */
public class SystemUI {


    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置状态栏颜色的activity
     * @param color    状态栏颜色
     */
    public static void setStatusColor(Activity activity, @ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLollipopStatusColor(activity, color);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setKitkatStatusColor(activity, color);
        }
    }


    /**
     * 设置状态栏颜色,并且设置根布局{@link View#setFitsSystemWindows(boolean)}为true
     *
     * @param activity    需要设置状态栏颜色的activity
     * @param color       状态栏颜色
     * @param offsetViews 需要竖直偏移的view,因为设置颜色之后,activity的布局会顶到状态栏里面,
     *                    使用该方法可以将指定的view竖直偏移状态栏的高度
     */
    public static void setStatusColor(Activity activity, @ColorInt int color, View... offsetViews) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            setLollipopStatusColor(activity, color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setKitkatStatusColor(activity, color);
        }

        verticalOffsetViews(activity, offsetViews);
    }


    /**
     * 使用{@link Window#addFlags(int)}{@link WindowManager.LayoutParams#FLAG_TRANSLUCENT_STATUS},
     * 配合添加一个设置为指定颜色的view到状态栏区域来间接设置状态栏颜色
     *
     * @param activity 需要设置状态栏颜色的activity
     * @param color    状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setKitkatStatusColor(Activity activity, @ColorInt int color) {

        Window window = activity.getWindow();
        //延伸到状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //添加一个view
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        decorViewGroup.addView(statusBarView);
    }


    /**
     * 使用{@link Window#setStatusBarColor(int)}API设置状态栏颜色
     *
     * @param activity 需要设置状态栏颜色的activity
     * @param color    状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setLollipopStatusColor(Activity activity, @ColorInt int color) {

        Window window = activity.getWindow();
        //设置状态栏颜色必须清除WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS标记
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏颜色必须设置WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS标记
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //绘制透明状态栏
        window.setStatusBarColor(color);

        //activity背景延伸到状态栏,状态栏不隐藏,布局稳定
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }


    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 高度
     */
    public static int getStatusBarHeight(Context context) {

        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    /**
     * 竖直方向偏移状态栏距离
     *
     * @param views 需要偏移的views
     */
    public static void verticalOffsetViews(Context context, View... views) {

        if (views != null) {
            int height = getStatusBarHeight(context);
            int length = views.length;
            for (int i = 0; i < length; i++) {
                View view = views[i];
                verticalOffsetView(view, height);
            }
        }
    }


    /**
     * 竖直方向偏移状态栏距离
     *
     * @param view            需要偏移的view
     * @param statusBarHeight 状态栏高度
     */
    public static void verticalOffsetView(View view, int statusBarHeight) {

        if (view != null) {
            ViewGroup.LayoutParams rootLayoutParams = view.getLayoutParams();
            if (rootLayoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)
                        rootLayoutParams;
                layoutParams.topMargin += statusBarHeight;
            } else {
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() + statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom()
                );
            }
            view.requestLayout();
        }
    }


    /**
     * 设置状态栏半透明,并且activity布局延伸到状态栏下面
     *
     * @param activity activity
     */
    public static void translucentStatus(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 状态栏开始隐藏,activity布局占据状态栏位置,在状态栏位置下拉,状态栏出现,会引发activity布局向下移动
     *
     * @param activity activity
     */
    public static void hideStatus(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    /**
     * 状态栏开始隐藏,activity布局不占据状态栏位置,在状态栏位置下拉,状态栏出现,不会引发activity布局向下移动
     *
     * @param activity activity
     */
    public static void hideStatusStable(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }


    /**
     * 状态栏开始隐藏,activity布局占据状态栏位置,在状态栏位置下拉,状态栏出现,不会引发activity布局向下移动
     *
     * @param activity activity
     */
    public static void layoutFullScreen(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }


    /**
     * 隐藏导航条,activity布局占据导航条位置,点击屏幕出现,会触发activity布局向上移动
     *
     * @param activity activity
     */
    public static void hideNavigation(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }


    /**
     * 隐藏导航条,activity布局占据导航条位置,点击屏幕出现,不会触发activity布局向上移动
     *
     * @param activity activity
     */
    public static void layoutHideNavigation(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }


    /**
     * 同时隐藏状态栏导航条,下拉出现,之后不会消失
     *
     * @param activity activity
     */
    public static void immersive(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    /**
     * 同时隐藏状态栏导航条,下拉出现(状态栏导航条半透明),之后会消失
     *
     * @param activity activity
     */
    public static void immersiveSticky(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 同时隐藏导航条状态栏,点击后出现,activity布局重置
     *
     * @param activity activity
     */
    public static void hideSystemUI(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    /**
     * 同时隐藏导航条状态栏,点击后出现,activity布局不重置
     *
     * @param activity activity
     */
    public static void hideSystemUiStable(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }


    public static void normal(Activity activity) {

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_VISIBLE
        );
    }
}
