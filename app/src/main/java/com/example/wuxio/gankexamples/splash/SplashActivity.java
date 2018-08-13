package com.example.wuxio.gankexamples.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.root.RootActivity;
import tech.threekilogram.messengers.Messengers;
import tech.threekilogram.messengers.OnMessageReceiveListener;

/**
 * show splash ,than goto mainActivity
 *
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity {

      protected ImageView mLogoImage;
      protected TextView  mCountText;
      private   CountDown mCountDownMachine;

      /**
       * 用于和数据层交互
       */
      private SplashManager mManager;

      public static void start ( Context context ) {

            Intent starter = new Intent( context, SplashActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_splash );
            initView();
            postAction();
      }

      private void initView ( ) {

            mLogoImage = findViewById( R.id.logoImage );
            mCountText = findViewById( R.id.countText );
      }

      /**
       * view 创建好之后执行方法里操作
       */
      private void postAction ( ) {

            mLogoImage.post( ( ) -> {

                  if( mCountDownMachine == null ) {
                        mCountDownMachine = new CountDown();
                  }
                  mCountDownMachine.start();
            } );
      }

      @Override
      public void onBackPressed ( ) {

            RootActivity.start( this );
            finish();
            super.onBackPressed();
      }

      //============================ 加载Splash图片 ============================

      @Override
      public void finish ( ) {

            /* 释放资源 */

            mCountDownMachine.cancel();
            super.finish();
      }

      //============================ 跳转到主界面 ============================

      /**
       * 倒计时
       *
       * @param counted 剩余时间
       */
      private void setCountDownText ( int counted ) {

            mCountText.setText( getString( R.string.jump, counted ) );
      }

      /**
       * 跳转到{@link MainActivity}
       */
      public void toMainActivity ( View view ) {

            MainActivity.start( this );
            finish();
            overridePendingTransition( R.anim.screen_fade_in, R.anim.screen_zoom_out );
      }

      // ========================= 倒计时 =========================

      private class CountDown implements OnMessageReceiveListener {

            private int mWhat      = 3;
            private int mCountTime = 3;

            void start ( ) {

                  Messengers.send( mWhat, this );
            }

            void cancel ( ) {

                  Messengers.remove( mWhat, this );
            }

            @Override
            public void onReceive ( int what, Object extra ) {

                  int left = mCountTime--;

                  setCountDownText( left );

                  if( left <= 0 ) {

                        //toMainActivity( null );
                  } else {

                        Messengers.send( mWhat, 1000, this );
                  }
            }
      }
}