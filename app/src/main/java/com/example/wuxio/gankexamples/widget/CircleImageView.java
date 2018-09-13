package com.example.wuxio.gankexamples.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.threekilogram.viewskin.shape.CircleClipper;

/**
 * 圆角imageView
 *
 * @author Liujin 2018-09-12:15:05
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

      private CircleClipper<CircleImageView> mClipper;
      private Paint                          mPaint;

      public CircleImageView ( Context context ) {

            this( context, null, 0 );
      }

      public CircleImageView (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public CircleImageView ( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init();
      }

      private void init ( ) {

            mClipper = new CircleClipper<>( this );
            mPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
            mPaint.setStyle( Style.STROKE );
            mPaint.setStrokeWidth( 10 );
            mPaint.setColor( Color.WHITE );
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            mClipper.clipCanvas( canvas );
            super.onDraw( canvas );

            int size = getWidth();
            int radius = size >> 1;
            canvas.drawCircle( radius, radius, radius - mPaint.getStrokeWidth() / 2, mPaint );
      }
}
