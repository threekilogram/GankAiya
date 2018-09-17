package com.example.wuxio.gankexamples.root;

import java.util.ArrayList;

/**
 * @author Liujin 2018-09-17:21:31
 */
public class OnAppExitManager {

      private static ArrayList<OnAppExitListener> sListeners = new ArrayList<>();

      /**
       * 退出app,由{@link RootActivity#finish()}回调
       */
      static void onExitApp ( ) {

            for( OnAppExitListener listener : sListeners ) {
                  listener.onExit();
            }
            sListeners.clear();
      }

      /**
       * 添加监听
       */
      public static void addListener ( OnAppExitListener listener ) {

            sListeners.add( listener );
      }

      /**
       * 监听app退出
       */
      public interface OnAppExitListener {

            /**
             * app 退出时回调
             */
            void onExit ( );
      }
}
