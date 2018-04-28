package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.system_ui.SystemUI;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.RootActivity;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;
import com.example.wuxio.gankexamples.utils.image.RoundBitmapFactory;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    protected DrawerLayout   mDrawer;
    protected LinearLayout   mContent;
    protected NavigationView mNavigationView;


    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        initView();
        setSystemUI();
        postAction();
    }


    private void initView() {

        mDrawer = findViewById(R.id.drawer);
        mContent = findViewById(R.id.content);
        mNavigationView = findViewById(R.id.navigationView);

    }


    private void postAction() {

        mDrawer.post(new Runnable() {
            @Override
            public void run() {

                initNavigationView(mNavigationView);
            }
        });
    }


    /**
     * 设置navigation布局
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

        NavigationItemClickListener clickListener = new NavigationItemClickListener();
        headerView.findViewById(R.id.toAbout).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toQuestFeedback).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toDonate).setOnClickListener(clickListener);
        headerView.findViewById(R.id.toLoginGithub).setOnClickListener(clickListener);
        headerView.findViewById(R.id.exitApp).setOnClickListener(clickListener);
    }


    /**
     * 沉浸式状态栏
     */
    private void setSystemUI() {

        SystemUI.setStatusColor(this, Color.TRANSPARENT);
    }


    private void closeDrawer() {

        mDrawer.closeDrawer(Gravity.START);
    }


    /**
     * to about page
     */
    private void toAbout() {

        Log.i(TAG, "toAbout:" + "");
        closeDrawer();
    }


    /**
     * to QuestionFeedback page
     */
    private void toQuestionFeedback() {

        Log.i(TAG, "toQuestionFeedback:" + "");
        closeDrawer();
    }


    /**
     * to Donate page
     */
    private void toDonate() {

        Log.i(TAG, "toDonate:" + "");
        closeDrawer();
    }


    /**
     * to LoginGitHub page
     */
    private void toLoginGitHub() {

        Log.i(TAG, "toLoginGitHub:" + "");
        closeDrawer();
    }


    /**
     * to exitApp
     */
    private void exitApp() {

        Log.i(TAG, "exitApp:" + "");
        RootActivity.start(this);
        closeDrawer();
    }

    //============================ 内部类 ============================

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
        }
    }

}