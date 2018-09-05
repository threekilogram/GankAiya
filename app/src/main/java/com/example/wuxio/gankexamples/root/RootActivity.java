package com.example.wuxio.gankexamples.root;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.bitmapreader.ScreenSize;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.GankModel;
import com.example.wuxio.gankexamples.splash.SplashActivity;
import com.threekilogram.objectbus.executor.PoolExecutor;
import com.threekilogram.systemui.SystemUi;
import tech.threekilogram.network.state.manager.NetStateChangeManager;

/**
 * 作为根activity,使用singleTask模式,用来清除任务栈
 *
 * @author wuxio
 */
public class RootActivity extends AppCompatActivity {

      private static final String TAG = "RootActivity";

      public static void start ( Context context ) {

            Intent starter = new Intent( context, RootActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );

            SplashActivity.start( this );

            SystemUi.transparentStatus( RootActivity.this );

            PoolExecutor.execute( ( ) -> {
                  /* 注册一个网络状态监听器,因为之后的界面都需要网络,所以越早注册越好 */
                  NetStateChangeManager.registerReceiver( App.INSTANCE );
                  GankModel.init( App.INSTANCE );
                  ScreenSize.init( App.INSTANCE );
            } );
      }

      @Override
      protected void onNewIntent ( Intent intent ) {

            super.onNewIntent( intent );
            finish();
      }

      @Override
      public void finish ( ) {

            /* 应用退出之后,不再需要监视网络状态 */
            NetStateChangeManager.unRegisterReceiver( this );
            super.finish();
      }
}
