package com.example.wuxio.gankexamples.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 圆角imageView
 *
 * @author Liujin 2018-09-12:15:05
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

      private Paint mPaint;
      private Path  mPath;

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

            mPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
            mPaint.setStyle( Style.STROKE );
            mPaint.setStrokeWidth( 10 );
            mPaint.setColor( Color.WHITE );

            mPath = new Path();
      }

      @Override
      protected void onSizeChanged ( int w, int h, int oldw, int oldh ) {

            super.onSizeChanged( w, h, oldw, oldh );

            mPath.addCircle( w >> 1, h >> 1, w >> 1, Direction.CW );
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            canvas.clipPath( mPath );
            super.onDraw( canvas );

            int size = getWidth();
            int radius = size >> 1;
            canvas.drawCircle( radius, radius, radius - mPaint.getStrokeWidth() / 2, mPaint );
      }
}
