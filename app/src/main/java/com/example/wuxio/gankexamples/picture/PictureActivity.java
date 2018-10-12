package com.example.wuxio.gankexamples.picture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.wuxio.gankexamples.R;
import java.util.List;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      public static void start (
          Activity context, View view, int startIndex, List<Bitmap> bitmaps ) {

            PictureModel.setStartData( startIndex, bitmaps );

            Intent starter = new Intent( context, PictureActivity.class );
            context.startActivity(
                starter,
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation( context, view, view.getTransitionName() )
                    .toBundle()
            );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_picture );

            PictureModel.bind( this );
            initView();
      }

      private void initView ( ) {

      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }
}
