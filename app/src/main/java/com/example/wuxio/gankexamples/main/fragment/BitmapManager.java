package com.example.wuxio.gankexamples.main.fragment;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-16:21:25
 */
public class BitmapManager {

    private static final String TAG = "BitmapManager";


    public void getAndSet(String url, ImageView imageView) {

        WeakReference< ImageView > reference = new WeakReference<>(imageView);

        ObjectBus bus = BusStation.callNewBus();

        bus.toUnder(() -> {

            Bitmap bitmap = BitmapReader.decodeSampledBitmap(
                    App.INSTANCE.getResources(),
                    R.drawable.homepage_header,
                    50,
                    50
            );
            Log.i(TAG, "run:" + url + " " + bitmap.getAllocationByteCount());

            bus.take(bitmap, "temp");

        }).toMain(() -> {

            Bitmap bitmap = (Bitmap) bus.getOff("temp");
            ImageView view = reference.get();
            if (view != null
                    && view.getVisibility() != View.GONE
                    && url.equals(view.getTag(R.id.show_item_image))) {

                Log.i(TAG, "run:" + "set bitmap" + url);
                view.setImageBitmap(bitmap);
            }

            BusStation.recycle(bus);
        }).run();
    }

    //============================ singleTon ============================


    private BitmapManager() {

    }


    public static BitmapManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final BitmapManager INSTANCE = new BitmapManager();
    }

    //============================ 辅助类 ============================

    /**
     * 记录bitmap 信息
     */
    private class ReferencesToBitmap {

        private WeakReference< Bitmap > mBitmapRef;

        /**
         * bitmap width
         */
        private int width;
        /**
         * bitmap height
         */
        private int height;

        private WeakReference< ImageView > mImageViewRef;
    }

}
