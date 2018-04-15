package com.example.wuxio.gankexamples;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.wuxio.gankexamples.utils.GlideUtils;

/**
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity {

    protected ImageView mImageView;

    private static Handler splashHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        initView();
    }


    public void toMain(View view) {

        splashHandler.removeCallbacksAndMessages(null);
        MainActivity.start(this);
        finishWithAnim();
    }


    private void initView() {

        mImageView = findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.img_transition_default);
        int timeout = 3;
        tryToLoadSplashImg(timeout);
        toMainDelayed(timeout * 1000);
    }


    private void tryToLoadSplashImg(int timeout) {

        mImageView.post(
                () -> GlideUtils.loadSplashImg(
                        timeout,
                        mImageView)
        );
    }


    private void toMainDelayed(int delayed) {

        splashHandler.postDelayed(
                () -> toMain(null),
                delayed
        );
    }


    private void finishWithAnim() {

        overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_zoom_out);
        finish();
    }


    @Override
    public void onBackPressed() {
        splashHandler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }
}