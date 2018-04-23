package com.example.wuxio.gankexamples;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wuxio.gankexamples.utils.SystemUI;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    protected DrawerLayout mDrawer;
    protected LinearLayout mContent;
    private   boolean      setFlag;


    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        initView();

        SystemUI.setStatusColor(this, Color.TRANSPARENT, mContent);
    }


    private void initView() {

        mDrawer = findViewById(R.id.drawer);
        mContent = findViewById(R.id.content);
    }


    public void changeSystemUI(View view) {

    }
}