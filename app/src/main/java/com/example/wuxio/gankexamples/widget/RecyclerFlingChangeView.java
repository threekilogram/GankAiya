package com.example.wuxio.gankexamples.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Liujin 2018-10-15:17:20
 */
public class RecyclerFlingChangeView extends RecyclerView {

      private float mFlingScale = 1f;

      public RecyclerFlingChangeView ( Context context ) {

            super( context );
      }

      public RecyclerFlingChangeView (
          Context context, @Nullable AttributeSet attrs ) {

            super( context, attrs );
      }

      public RecyclerFlingChangeView (
          Context context, @Nullable AttributeSet attrs, int defStyle ) {

            super( context, attrs, defStyle );
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
}
