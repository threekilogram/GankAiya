package com.example.wuxio.gankexamples.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Liujin 2018-10-15:17:20
 */
public class RecyclerFlingChangeView extends RecyclerView {

      private float mFlingScale = 1f;

      public RecyclerFlingChangeView ( Context context ) {

            this( context, null, 0 );
      }

      public RecyclerFlingChangeView (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public RecyclerFlingChangeView (
          Context context, @Nullable AttributeSet attrs, int defStyle ) {

            super( context, attrs, defStyle );
            setSaveEnabled( true );
      }

      @Override
      public boolean fling ( int velocityX, int velocityY ) {

            return super
                .fling( (int) ( mFlingScale * velocityX ), (int) ( mFlingScale * velocityY ) );
      }

      public void setFlingScale ( float flingScale ) {

            mFlingScale = flingScale;
      }

      public float getFlingScale ( ) {

            return mFlingScale;
      }

      @Override
      protected Parcelable onSaveInstanceState ( ) {

            return super.onSaveInstanceState();
      }

      @Override
      protected void onRestoreInstanceState ( Parcelable state ) {

            super.onRestoreInstanceState( state );
      }
}
