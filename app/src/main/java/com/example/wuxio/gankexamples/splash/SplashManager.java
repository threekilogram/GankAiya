package com.example.wuxio.gankexamples.splash;

import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.ImageView;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.action.DecodeSampledBitmapRunnable;
import com.example.wuxio.gankexamples.action.ImageCallable;
import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

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

                    ImageCallable imageCallable = new ImageCallable(SplashImageManager.getUrl());
                    Pair< String, File > filePair = imageCallable.call();

                    Bitmap bitmap;
                    if (filePair != null) {
                        File imageFile = filePair.second;
                        DecodeSampledBitmapRunnable decodeSampledBitmapRunnable =
                                new DecodeSampledBitmapRunnable(
                                        imageFile,
                                        width,
                                        height,
                                        R.drawable.without_net
                                );

                        decodeSampledBitmapRunnable.run();
                        bitmap = decodeSampledBitmapRunnable.getBitmap();

                    } else {

                        bitmap = BitmapReader.decodeSampledBitmap(
                                App.INSTANCE.getResources(),
                                R.drawable.without_net,
                                width,
                                height
                        );

                    }

                    mBus.take(bitmap, "bitmap");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

                try {

                    Bitmap bitmap = (Bitmap) mBus.getOff("bitmap");
                    getActivity().setSplashBitmap(bitmap);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).run();
    }
}
