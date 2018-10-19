package com.example.wuxio.gankexamples.utils;

import android.widget.Toast;
import com.example.wuxio.gankexamples.App;
import com.threekilogram.objectbus.executor.MainExecutor;
import java.lang.ref.WeakReference;

/**
 * @author Liujin 2018-10-14:15:05
 */
public class ToastMessage {

      private static WeakReference<Toast> sRef;

      public static void toast ( String message ) {

            MainExecutor.execute( ( ) -> {

                  if( sRef != null && sRef.get() != null ) {
                        sRef.get().cancel();
                  }

                  Toast toast = Toast.makeText( App.INSTANCE, message, Toast.LENGTH_SHORT );
                  toast.show();
                  sRef = new WeakReference<>( toast );
            } );
      }
}
