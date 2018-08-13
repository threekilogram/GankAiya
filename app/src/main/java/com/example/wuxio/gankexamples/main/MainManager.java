package com.example.wuxio.gankexamples.main;

import com.example.wuxio.gankexamples.BaseManager;
import com.threekilogram.banner.BannerView;

/**
 * @author wuxio 2018-05-02:15:24
 */
public class MainManager extends BaseManager< MainActivity > {

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
    public void onStart() {

        MainActivity activity = get();
        BannerView banner = activity.getBanner();
        int width = banner.getWidth();
        int height = banner.getHeight();

//        ObjectBus bus = ObjectBusStation.callNewBus();
//
//        bus.toUnder(() -> {
//
//            /* model get new data */
//            List< GankCategoryBean > beauty = ModelManager.getInstance().loadBeauty();
//
//            int size = beauty.size();
//            ArrayList< Bitmap > bitmaps = new ArrayList<>();
//            for (int i = 0; i < size; i++) {
//                String url = beauty.get(i).url;
//
//                Bitmap bitmap = UrlToBitmapAction.loadUrlToBitmap(url, width, height);
//                bitmaps.add(bitmap);
//
//            }
//
//            bus.take(bitmaps, "temp");
//
//        }).toMain(() -> {
//
//            /* told MainActivity banner data start index */
//
//            try {
//
//                ArrayList< Bitmap > bitmaps = (ArrayList< Bitmap >) bus.getOff("temp");
//
//                get().notifyBannerDataChanged(0, bitmaps);
//
//            } catch (NullPointerException e) {
//
//                e.printStackTrace();
//            }
//
//            /* all task finished recycle bus */
//            ObjectBusStation.recycle(bus);

//        }).run();
    }
}
