package com.example.wuxio.gankexamples.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.wuxio.gankexamples.R;
import com.threekilogram.drawable.anim.BiliBiliLoadingDrawable;

/**
 * @author Liujin 2018-10-15:14:12
 */
public class LoadingView extends View {

      private static BiliBiliLoadingDrawable sLoadingDrawable;

      private boolean isStart;

      public LoadingView ( Context context ) {

            this( context, null, 0 );
      }

      public LoadingView (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public LoadingView ( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init();
      }

      private void init ( ) {

            if( sLoadingDrawable == null ) {
                  sLoadingDrawable = new BiliBiliLoadingDrawable(
                      getResources().getDimensionPixelSize( R.dimen.banner_loading_size ) );
                  sLoadingDrawable.setStrokeWidth( 5 );
                  sLoadingDrawable.setDuration( 2400 );
                  sLoadingDrawable.setRepeat( Integer.MAX_VALUE / 2400 );
                  sLoadingDrawable.setRadius( 7 );
                  sLoadingDrawable.setPaintColor( getResources().getColor( R.color.blue ) );
                  sLoadingDrawable.start();
            }
      }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {

            setMeasuredDimension(
                sLoadingDrawable.getIntrinsicWidth(),
                sLoadingDrawable.getIntrinsicHeight()
            );
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            sLoadingDrawable.draw( canvas );

            if( isStart ) {
                  invalidate();
            }
      }

      @Override
      protected void onVisibilityChanged (
          @NonNull View changedView, int visibility ) {

            super.onVisibilityChanged( changedView, visibility );

            if( visibility == GONE || visibility == INVISIBLE ) {
                  isStart = false;
            } else {
                  isStart = true;
                  invalidate();
            }
      }

      @Override
      protected void onDetachedFromWindow ( ) {

            stop();
            super.onDetachedFromWindow();
      }

      public void start ( ) {

            isStart = true;
            invalidate();
      }

      public void stop ( ) {

            isStart = false;
      }
}
