package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.example.wuxio.gankexamples.R;
import tech.threekilogram.viewpager.adapter.BasePagerAdapter;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {

      protected ViewPager           mViewPager;
      private   PicturePagerAdapter mPicturePagerAdapter;

      public static void start ( Context context ) {

            Intent starter = new Intent( context, PictureActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_picture );

            initView();
      }

      private void initView ( ) {

            mViewPager = findViewById( R.id.viewPager );
      }

      public void notifyBitmapsChanged ( int position ) {

            if( mPicturePagerAdapter == null ) {

                  mPicturePagerAdapter = new PicturePagerAdapter();
                  mViewPager.setAdapter( mPicturePagerAdapter );
                  mViewPager.addOnPageChangeListener( new PicturePagerOnPageChangeListener() );
            }

            mPicturePagerAdapter.notifyDataSetChanged();
            if( position > 0 ) {
                  mViewPager.setCurrentItem( position );
            }
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      //============================ ViewPagerAdapter ============================

      private class PicturePagerAdapter extends BasePagerAdapter<Bitmap, ImageView> {

            @Override
            public int getCount ( ) {

                  return 5;
            }

            @Override
            protected Bitmap getData ( int i ) {

                  return null;
            }

            @Override
            protected ImageView getView ( ViewGroup viewGroup, int i ) {

                  ImageView imageView = new ImageView( PictureActivity.this );
                  imageView.setScaleType( ScaleType.CENTER_INSIDE );
                  return imageView;
            }

            @Override
            protected void bindData ( int i, Bitmap bitmap, ImageView view ) {

                  view.setImageResource( R.drawable.a42 );
            }
      }

      //============================ pager scroll Listener ============================

      private class PicturePagerOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

            @Override
            public void onPageSelected ( int position ) {

            }
      }
}
