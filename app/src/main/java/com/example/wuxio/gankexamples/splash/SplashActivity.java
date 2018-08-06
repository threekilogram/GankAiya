package com.example.wuxio.gankexamples.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.objectbus.message.Messengers;
import com.example.objectbus.message.OnMessageReceiveListener;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.root.RootActivity;

/**
 * show splash ,than goto mainActivity
 *
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity implements OnMessageReceiveListener {

    protected ImageView     mLogoImage;
    protected TextView      mCountText;
    private   SplashManager mManager;

    private static final int MSG_WHAT_COUNT = 3;
    private              int mCountDown     = 3;


    public static void start(Context context) {

        Intent starter = new Intent(context, SplashActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        initView();


        /* 委托manager加载splash图片 */
//        mManager = new SplashManager();
//        mManager.register(this);
//        postAction();
    }


    private void initView() {

        mLogoImage = findViewById(R.id.logoImage);
        mCountText = findViewById(R.id.countText);
    }


    /**
     * view 创建好之后执行方法里操作
     */
    private void postAction() {

        mLogoImage.post(() -> {

            /* 加载splash图片 */
            mManager.loadLogoImage();

            /* 倒计时 */
            Messengers.send(MSG_WHAT_COUNT, SplashActivity.this);

        });
    }


    @Override
    public void onBackPressed() {

        RootActivity.start(this);
        finish();
        super.onBackPressed();
    }

    //============================ 加载Splash图片 ============================


    public ImageView getLogoImage() {

        return mLogoImage;
    }


    public void setSplashBitmap(Bitmap bitmap) {

        mLogoImage.setImageBitmap(bitmap);
    }

    //============================ 更新倒计时 ============================

      @Override
      public void onReceive (int what, Object extra) {

      }

    @Override
    public void onReceive(int what) {

        if (MSG_WHAT_COUNT == what) {

            if (mCountDown <= 0) {
                countDown(mCountDown);
                toMainActivity(null);
                return;
            }

            countDown(mCountDown);
            mCountDown--;
            Messengers.send(MSG_WHAT_COUNT, 1000, this);
        }
    }


    /**
     * 倒计时
     *
     * @param counted 剩余时间
     */
    private void countDown(int counted) {

        mCountText.setText(getString(R.string.jump, counted));
    }

    //============================ 跳转到主界面 ============================


    /**
     * 跳转到{@link MainActivity}
     */
    public void toMainActivity(View view) {

        MainActivity.start(this);
        finish();
        overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_zoom_out);
    }


    @Override
    public void finish() {

        /* 释放资源 */

        mManager.unRegister();
        Messengers.remove(MSG_WHAT_COUNT, this);
        super.finish();
    }
}