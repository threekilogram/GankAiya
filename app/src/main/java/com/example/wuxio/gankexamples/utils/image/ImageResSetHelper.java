package com.example.wuxio.gankexamples.utils.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 使用imageView 的 android:src 属性加载图片时,
 * 即使指定了图片缩放模式,系统还是先加载图片到内存,然后缩放,
 * 使用此工具会缩放图片,然后在加载设置给imageView,优化内存
 *
 * @author wuxio
 * @date 2017-12-25
 */
public class ImageResSetHelper {

    /**
     * @param view  imageView
     * @param resID 资源id
     */
    public static void setBitmap(final ImageView view, final int resID) {

        Runnable runnable = new Runnable() {
            public Bitmap mBitmap;


            @Override
            public void run() {

                mBitmap = BitmapReader.decodeMaxSampledBitmap(
                        view.getContext().getResources(),
                        resID,
                        view.getWidth(),
                        view.getHeight()
                );

                view.setImageBitmap(mBitmap);
            }
        };
        view.post(runnable);
    }
}
