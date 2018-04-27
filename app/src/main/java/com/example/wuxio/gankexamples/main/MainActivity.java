package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.constraintlayout.ConstraintLayout;
import com.example.system_ui.SystemUI;
import com.example.wuxio.gankexamples.R;

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
    }


    private void initView() {

        mDrawer = findViewById(R.id.drawer);
        mContent = findViewById(R.id.content);
        mNavigationView = findViewById(R.id.navigationView);
        setHeader(mNavigationView);

    }


    /**
     * 设置navigation布局
     *
     * @param navigationView 导航view
     */
    private void setHeader(NavigationView navigationView) {

        navigationView.inflateHeaderView(R.layout.main_navi_header);

        /* set navigation Layout */

        ConstraintLayout constraintLayout = navigationView
                .getHeaderView(0)
                .findViewById(R.id.constraintLayout);
        constraintLayout.setAdapter(
                new NavigationViewAdapter(MainActivity.this, constraintLayout)
        );
    }


    private void setSystemUI() {

        SystemUI.setStatusColor(this, Color.TRANSPARENT);
    }

}