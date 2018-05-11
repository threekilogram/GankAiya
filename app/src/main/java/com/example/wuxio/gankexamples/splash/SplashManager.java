package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.action.DecodeSampledBitmapAction;
import com.example.wuxio.gankexamples.action.UrlToFileAction;

import java.io.File;

/**
 * @author wuxio 2018-05-10:10:12
 */
public class SplashManager extends BaseActivityManager< SplashActivity > {

    //============================ care ============================

    ObjectBus mBus = new ObjectBus();


    @Override
    public void onActivityCreate() {

    }


    /**
     * load background image from net
     */
    @SuppressWarnings("ConstantConditions")
    public void loadLogoImage() {

        ImageView image = getActivity().getLogoImage();
        int width = image.getWidth();
        int height = image.getHeight();

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                try {

                    File file = UrlToFileAction.loadUrlToFile(SplashImageManager.getUrl());

                    if (file != null) {

                        Bitmap bitmap = DecodeSampledBitmapAction.decode(
                                file,
                                width,
                                height,
                                R.drawable.without_net
                        );

                        mBus.take(bitmap, "bitmap");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

                try {

                    Bitmap bitmap = (Bitmap) mBus.getOff("bitmap");
                    if (bitmap != null) {
                        getActivity().setSplashBitmap(bitmap);
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).run();
    }
}
