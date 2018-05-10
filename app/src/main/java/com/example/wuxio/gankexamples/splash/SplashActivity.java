package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.root.RootActivity;
import com.example.wuxio.gankexamples.main.MainActivity;

import java.lang.ref.WeakReference;

/**
 * show splash ,than goto mainActivity
 *
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity {

    protected ImageView   mLogoImage;
    protected TextView    mCountText;
    protected FrameLayout mRoot;

    protected Bitmap       mBitmap;
    private   CountHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        initView();
        mHandler = new CountHandler(this);

        postAction();
    }


    @Override
    protected void onDestroy() {

        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    private void initView() {

        mRoot = findViewById(R.id.root);
        mLogoImage = findViewById(R.id.logoImage);
        mCountText = findViewById(R.id.countText);

    }


    /**
     * view 创建好之后执行方法里操作
     */
    private void postAction() {

        mRoot.post(() -> {

            loadLogoImage();
            mHandler.startCountDown(3);

        });
    }


    public ImageView getLogoImage() {

        return mLogoImage;
    }


    public void setSplash(Bitmap bitmap) {

        mBitmap = bitmap;
        mHandler.setSplash();
    }


    /**
     * load background image from net
     */
    private void loadLogoImage() {

        ObjectBus bus = new ObjectBus();
        LoadSplashPicRunnable runnable = new LoadSplashPicRunnable(this);
        bus.toUnder(runnable).run();
    }


    /**
     * 跳转到{@link MainActivity}
     */
    public void toRootActivity(View view) {

        RootActivity.start(this);
        finish();
        overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_zoom_out);
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 倒计时
     *
     * @param counted 剩余时间
     */
    private void countDown(int counted) {

        mCountText.setText(getString(R.string.jump, counted));
    }


    /**
     * 倒计时结束
     */
    private void countToEnd() {

        toRootActivity(null);
    }

    //============================ static Handler ============================

    private static class CountHandler extends Handler {

        WeakReference< SplashActivity > mReference;

        private static final int MSG_COUNT_DOWN = 12;
        private static final int MSG_SET_SPLASH = 13;

        private int countToDown;


        public CountHandler(SplashActivity activity) {

            mReference = new WeakReference<>(activity);
        }


        @Override
        public void handleMessage(Message msg) {

            SplashActivity activity = mReference.get();
            if (activity == null) {
                removeCallbacksAndMessages(null);
                return;
            }

            switch (msg.what) {

                case MSG_COUNT_DOWN:
                    handleMsgCountDown(activity);
                    break;

                case MSG_SET_SPLASH:
                    activity.getLogoImage().setImageBitmap(activity.mBitmap);

                default:
                    break;
            }
        }


        private void handleMsgCountDown(SplashActivity activity) {

            activity.countDown(countToDown);
            if (--countToDown >= 0) {
                loop();
                return;
            }
            activity.countDown(0);
            activity.countToEnd();
        }


        public void startCountDown(int second) {

            countToDown = second;
            sendEmptyMessage(MSG_COUNT_DOWN);
        }


        private void loop() {

            sendEmptyMessageDelayed(MSG_COUNT_DOWN, 1000);
        }


        public void setSplash() {

            sendEmptyMessage(MSG_SET_SPLASH);
        }
    }
}