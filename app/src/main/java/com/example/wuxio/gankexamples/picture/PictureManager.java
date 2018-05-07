package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.widget.ImageView;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.ActivityManager;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;
import java.util.List;

/**
 * @author wuxio 2018-05-07:14:44
 */
public class PictureManager extends ActivityManager< PictureActivity > {

    private int                      mBannerPosition;
    private List< String >           mUrls;
    private ArrayMap< String, File > mBitmapFileMap;

    private ObjectBus mBus = new ObjectBus();

    //============================ data ============================


    public void set(int position,
                    List< String > urls,
                    ArrayMap< String, File > bitmapFileMap) {

        mBannerPosition = position;
        mUrls = urls;
        mBitmapFileMap = bitmapFileMap;
    }


    public void clear() {

        mBitmapFileMap = null;
        mUrls = null;
        mBannerPosition = 0;
    }

    //============================ core ============================


    @Override
    public void onActivityCreate() {

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                PictureActivity activity = mReference.get();
                if (activity != null) {
                    ImageView imageView = activity.getImageView();
                    Bitmap bitmap = BitmapReader.decodeSampledBitmap(
                            mBitmapFileMap.get(mUrls.get(mBannerPosition)),
                            imageView.getWidth(),
                            imageView.getHeight()
                    );

                    mBus.take(bitmap, "key");
                }
            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

                PictureActivity activity = mReference.get();
                if (activity != null) {
                    activity.getImageView().setImageBitmap((Bitmap) mBus.getOff("key"));
                }
            }
        }).run();
    }

    //============================ singleTon ============================


    private PictureManager() {

    }


    public static PictureManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final PictureManager INSTANCE = new PictureManager();
    }
}
