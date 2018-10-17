package com.example.wuxio.gankexamples.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.wuxio.gankexamples.R;

/**
 * @author liujin
 */
public class AboutAppActivity extends AppCompatActivity {

      public static void start ( Context context ) {

            Intent starter = new Intent( context, AboutAppActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_about_app );
      }
}
