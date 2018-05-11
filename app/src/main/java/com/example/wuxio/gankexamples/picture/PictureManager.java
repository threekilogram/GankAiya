package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;

import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;
import com.example.wuxio.gankexamples.action.UrlToBitmapAction;
import com.example.wuxio.gankexamples.model.GankCategoryBean;
import com.example.wuxio.gankexamples.model.ModelManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-07:14:44
 */
public class PictureManager extends BaseActivityManager< PictureActivity > {

    private static final String TAG = "PictureManager";

    private int            mBannerPosition;
    private int            mDataIndex;
    private List< Bitmap > mBitmaps;
    private ObjectBus      mBus;

    private static final int DATA_COUNT = 5;

    //============================ data ============================


    public void set(int dataIndex, int position) {

        mBannerPosition = position;
        mDataIndex = dataIndex;
    }


    public void clear() {

        mBannerPosition = 0;
        mBitmaps = null;
        if (mBus != null) {
            BusStation.getInstance().recycle(mBus);
        }
    }


    public List< Bitmap > getBitmaps() {

        return mBitmaps;
    }

    //============================ core ============================


    @Override
    public void onActivityCreate() {

        int width = getActivity().getBitmapWidth();
        int height = getActivity().getBitmapHeight();

        if (mBus == null) {
            mBus = BusStation.getInstance().obtainBus();
        }

        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>(DATA_COUNT);
        }

        mBus.toUnder(new Runnable() {
            @Override
            public void run() {

                List< GankCategoryBean > beans = ModelManager.getInstance().getCategoryBeauties();
                for (int i = 0; i < DATA_COUNT; i++) {
                    GankCategoryBean bean = beans.get(mDataIndex + i);
                    Bitmap bitmap = UrlToBitmapAction.loadUrlToBitmap(bean.url, width, height);
                    mBitmaps.add(bitmap);
                }

            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

                try {
                    getActivity().nofityBitmapsChanged(mBannerPosition);
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
