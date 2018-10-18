package com.example.wuxio.gankexamples.root;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.log.AppLog;
import com.example.wuxio.gankexamples.main.BeautyModel;
import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.main.fragment.CategoryModel;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.threekilogram.systemui.SystemUi;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.screen.ScreenSize;

/**
 * 作为根activity,使用singleTask模式,用来清除任务栈
 *
 * @author wuxio
 */
public class RootActivity extends AppCompatActivity {

      private static final String KEY_QUIT = "isQuit";

      /**
       * 启动
       */
      public static void quitApp ( Context context ) {

            Intent starter = new Intent( context, RootActivity.class );
            starter.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            starter.putExtra( KEY_QUIT, true );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );

            boolean quit = getIntent().getBooleanExtra( KEY_QUIT, false );
            if( quit ) {
                  finish();
                  AppLog.addLog( "app 退出" );
                  AppLog.saveLog();
            } else {

                  /* 状态栏透明 */
                  SystemUi.transparentStatus( RootActivity.this );
                  /* 注册一个网络状态监听器,因为之后的界面都需要网络,所以越早注册越好 */
                  NetStateChangeManager.registerReceiver( App.INSTANCE );
                  /* 计算屏幕尺寸 */
                  ScreenSize.init( App.INSTANCE );
                  /* 创建app文件夹 */
                  FileManager.init();
                  /* 初始化变量 */
                  BeanLoader.init();
                  BitmapManager.init();
                  /* 开线程初始化 */
                  PoolExecutor.execute( ( ) -> {

                        /* 初始化福利数据 */
                        BeautyModel.init();
                        CategoryModel.init();
                  } );

                  /* 立即启动splash */
                  MainActivity.start( this );
                  finish();
                  AppLog.addLog( "app 启动" );
            }
      }

      @Override
      protected void onDestroy ( ) {
            /* 应用退出之后,不再需要监视网络状态 */
            NetStateChangeManager.unRegisterReceiver( this );
            NetStateChangeManager.clearListener();
            /* 释放资源 */
            OnAppExitManager.onExitApp();
            super.onDestroy();
      }
}
