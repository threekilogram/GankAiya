package com.example.wuxio.gankexamples;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wuxio.gankexamples.constant.ConstantsImageUrl;

import java.util.Random;

/**
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity {

    private TextView       mTVCountDown;
    private ImageView      mImageView;
    private CountDownTimer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        initView();
    }


    private void initView() {

        mImageView = findViewById(R.id.IV_SplashBack);
        mTVCountDown = findViewById(R.id.TV_countDown);
        tryToLoadSplashImg();
        toMainDelayed(3000);
    }


    private void tryToLoadSplashImg() {

        String[] urls = ConstantsImageUrl.TRANSITION_URLS;
        int imageUrlIndex = new Random().nextInt(urls.length);
        Glide.with(this)
                .load(urls[imageUrlIndex])
                .placeholder(R.drawable.img_transition_default)
                .into(mImageView);
    }


    public void toMain(View view) {

        MainActivity.start(this);
        overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_zoom_out);
        finish();
    }


    private void toMainDelayed(int delayed) {

        mTimer = new SplashCountDown(delayed, 1000);
        mTimer.start();
    }


    @Override
    public void onBackPressed() {

        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onBackPressed();
    }

    //============================ nbl ============================

    class SplashCountDown extends CountDownTimer {

        public SplashCountDown(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {

            String countText = getResources().getString(R.string.jump, millisUntilFinished / 1000);
            mTVCountDown.setText(countText);
        }


        @Override
        public void onFinish() {

            toMain(null);
        }
    }
}