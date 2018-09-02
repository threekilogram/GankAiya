package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;
import android.util.ArrayMap;
import com.example.wuxio.gankexamples.BaseManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-07:14:44
 */
public class PictureManager extends BaseManager< PictureActivity > {

    private static final String TAG = "PictureManager";

    private int                                         mBannerPosition;
    private int                                         mDataIndex;
    private ArrayMap< String, WeakReference< Bitmap > > mBitmaps;
    private List< String >                              mUrls;

    private static final int DATA_COUNT = 5;

    //============================ data ============================


    public void set(int dataIndex, int position) {

        mBannerPosition = position;
        mDataIndex = dataIndex;
    }


    public void clear() {

        mBannerPosition = 0;
        mDataIndex = 0;
        mBitmaps.clear();
        mUrls.clear();

    }

    //============================ core ============================


    @Override
    public void onStart() {

        int width = get().getBitmapWidth();
        int height = get().getBitmapHeight();

        if (mBitmaps == null) {
            mBitmaps = new ArrayMap<>();
            mUrls = new ArrayList<>();
        }

//        ObjectBus bus = ObjectBusStation.callNewBus();
//        bus.toUnder(() -> {
//
//            List< GankCategoryBean > beans = ModelManager.getInstance().getCategoryBeauties();
//            for (int i = 0; i < DATA_COUNT; i++) {
//                GankCategoryBean bean = beans.get(mDataIndex + i);
//                String url = bean.url;
//                mUrls.add(url);
//                Bitmap bitmap = UrlToBitmapAction.loadUrlToBitmap(url, width, height);
//                mBitmaps.put(url, new WeakReference<>(bitmap));
//            }
//
//        }).toMain(() -> {
//
//            try {
//                get().nofityBitmapsChanged(mBannerPosition);
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//
//            ObjectBusStation.recycle(bus);
//        }).run();
    }

    //============================ 图片 ============================


    public int getItemCount() {

        return mUrls.size();
    }


    public void releaseBitmap(int position) {

        if (position < mUrls.size() - 1) {

            String s = mUrls.get(position);
            mBitmaps.get(s).clear();
        }
    }


    public Bitmap loadBitmap(int position) {

        String s = mUrls.get(position);

        Bitmap result = mBitmaps.get(s).get();

        if (result != null) {
            return result;
        }

        int width = get().getBitmapWidth();
        int height = get().getBitmapHeight();

          //result = UrlToBitmapAction.loadUrlToBitmap(s, width, height);
        mBitmaps.put(s, new WeakReference<>(result));
        return result;
    }

    //============================ 加载更多 ============================


    public void loadMore() {

        int width = get().getBitmapWidth();
        int height = get().getBitmapHeight();

//        ObjectBus bus = ObjectBusStation.callNewBus();
//        bus.toUnder(() -> {
//
//            try {
//
//                List< GankCategoryBean > moreBeauty = ModelManager.getInstance().loadMoreBeauty();
//
//                int size = moreBeauty.size();
//                int start = mUrls.size();
//                List< String > temp = new ArrayList<>(size - start);
//
//                for (int i = start; i < size; i++) {
//                    GankCategoryBean bean = moreBeauty.get(i);
//                    String url = bean.url;
//                    temp.add(url);
//                    Bitmap bitmap = UrlToBitmapAction.loadUrlToBitmap(url, width, height);
//                    mBitmaps.put(url, new WeakReference<>(bitmap));
//                }
//
//                bus.take(temp, "newData");
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
//
//        }).toMain(() -> {
//
//            try {
//
//                List< String > temp = (List< String >) bus.getOff("newData");
//                mUrls.addAll(temp);
//                get().nofityBitmapsChanged(-1);
//
//            } catch (NullPointerException e) {
//
//                e.printStackTrace();
//            }
//
//            ObjectBusStation.recycle(bus);
//
//        }).run();

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
