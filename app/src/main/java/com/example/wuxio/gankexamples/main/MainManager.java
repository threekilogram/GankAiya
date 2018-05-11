package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import android.util.Pair;

import com.example.banner.BannerView;
import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;
import com.example.wuxio.gankexamples.action.ImageCallable;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.ModelManager;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;
import java.util.List;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager extends BaseActivityManager< MainActivity > {

    private static final String TAG = "MainManager";

    //============================ singleTon ============================


    private MainManager() {

    }


    private static class SingletonHolder {
        private static final MainManager INSTANCE = new MainManager();
    }


    public static MainManager getInstance() {

        return SingletonHolder.INSTANCE;
    }

    //============================ core ============================


    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreate() {

        MainActivity activity = getActivity();
        BannerView banner = activity.getBanner();
        int width = banner.getWidth();
        int height = banner.getHeight();

        ObjectBus bus = BusStation.getInstance().obtainBus();
        bus.toUnder(() -> {

            /* model get new data */
            List< GankCategoryBean > beauty = ModelManager.getInstance().loadBeauty();

            int size = beauty.size();

            for (int i = 0; i < size; i++) {
                String url = beauty.get(i).url;
                File file = getPicFile(url);

                /* load Picture to Disk */

                if (!file.exists()) {

                    ImageCallable imageCallable = new ImageCallable(url);
                    Pair< String, File > call = imageCallable.call();
                }

                /* decode bitmap */

                Bitmap bitmap = BitmapReader.decodeSampledBitmap(file, width, height);

                /* put bitmap to container, banner data is also link to container,so banner will auto
                update */
                BannerBitmapManager.put(i, bitmap);
            }

        }).toMain(() -> {

            /* told MainActivity banner data start index */
            try {
                getActivity().notifyBannerDataChanged(0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            /* all task finished recycle bus */
            BusStation.getInstance().recycle(bus);
        }).run();
    }


    private File getPicFile(String url) {

        String name = FileNameUtils.makeName(url);
        File file = FileManager.getAppPicFile();
        File pic = new File(file, name);
        return pic;
    }
}
