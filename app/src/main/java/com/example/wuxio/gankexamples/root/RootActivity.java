package com.example.wuxio.gankexamples.root;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.GankModel;
import com.threekilogram.objectbus.executor.PoolExecutor;
import com.threekilogram.systemui.SystemUi;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.screen.ScreenSize;

/**
 * 作为根activity,使用singleTask模式,用来清除任务栈
 *
 * @author wuxio
 */
public class RootActivity extends AppCompatActivity {

      public static void start ( Context context ) {

            Intent starter = new Intent( context, RootActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );

            TextView textView = new TextView( this );
            textView.setBackgroundColor( Color.RED );
            setContentView( textView );

            /* 立即启动splash */
            //SplashActivity.start( this );
            /* 状态栏透明 */
            SystemUi.transparentStatus( RootActivity.this );
            /* 注册一个网络状态监听器,因为之后的界面都需要网络,所以越早注册越好 */
            NetStateChangeManager.registerReceiver( App.INSTANCE );

            PoolExecutor.execute( ( ) -> {

                  ScreenSize.init( App.INSTANCE );
                  GankModel.init( App.INSTANCE );
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
            NetStateChangeManager.clearListener();
            /* 释放资源 */
            OnAppExitManager.onExitApp();
            super.finish();
      }
}
