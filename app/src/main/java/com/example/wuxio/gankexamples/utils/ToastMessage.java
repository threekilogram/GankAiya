package com.example.wuxio.gankexamples.utils;

import android.widget.Toast;
import com.example.wuxio.gankexamples.App;
import com.threekilogram.objectbus.executor.MainExecutor;

/**
 * @author Liujin 2018-10-14:15:05
 */
public class ToastMessage {

      public static void toast ( String message ) {

            MainExecutor.execute( ( ) -> {
                  Toast.makeText( App.INSTANCE, message, Toast.LENGTH_SHORT ).show();
            } );
      }
}
