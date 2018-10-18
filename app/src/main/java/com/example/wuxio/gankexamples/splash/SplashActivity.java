package com.example.wuxio.gankexamples.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.log.AppLog;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.root.RootActivity;
import java.lang.ref.WeakReference;

/**
 * show splash ,than goto mainActivity
 *
 * @author wuxio
 */
public class SplashActivity extends AppCompatActivity {

      /**
       * logo
       */
      protected ImageView        mLogoImage;
      /**
       * countdown
       */
      protected TextView         mCountText;
      /**
       * count down timer
       */
      private   CountDownHandler mCountDowner;

      /**
       * quitApp
       */
      public static void start ( Context context ) {

            Intent starter = new Intent( context, SplashActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            AppLog.addLog( "splash 启动" );
            super.setContentView( R.layout.activity_splash );

            initView();
            postAction();

            SplashModel.bind( this );
            SplashModel.setSplashImage();
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

                  if( mCountDowner == null ) {
                        mCountDowner = new CountDownHandler( this );
                  }
                  mCountDowner.start();
            } );
      }

      @Override
      public void onBackPressed ( ) {

            RootActivity.quitApp( this );
            super.onBackPressed();
      }

      @Override
      protected void onDestroy ( ) {

            mCountDowner.cancel();
            AppLog.addLog( "splash 退出" );
            super.onDestroy();
      }

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

            //MainActivity.start( SplashActivity.this );
            //finish();
            //overridePendingTransition( R.anim.screen_fade_in, R.anim.screen_zoom_out );
      }

      /**
       * 倒计时
       */
      private static class CountDownHandler extends Handler {

            private int mWhat      = 3;
            private int mCountTime = 3;

            private WeakReference<SplashActivity> mRef;

            private CountDownHandler ( SplashActivity activity ) {

                  mRef = new WeakReference<>( activity );
            }

            void start ( ) {

                  sendEmptyMessage( mWhat );
            }

            void cancel ( ) {

                  removeCallbacksAndMessages( null );
            }

            @Override
            public void handleMessage ( Message msg ) {

                  super.handleMessage( msg );

                  int left = mCountTime--;

                  SplashActivity splashActivity = mRef.get();

                  if( splashActivity != null ) {

                        splashActivity.setCountDownText( left );

                        if( left <= 0 ) {

                              splashActivity.toMainActivity( null );
                        } else {

                              sendEmptyMessageDelayed( mWhat, 1000 );
                        }
                  } else {

                        removeCallbacksAndMessages( null );
                  }
            }
      }
}
