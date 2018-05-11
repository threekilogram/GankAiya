package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;

import com.example.objectbus.bus.BusStation;
import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.BaseActivityManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
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

    //============================ core ============================


    @Override
    public void onActivityCreate() {

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
                    String url = bean.url;
                    FileNameUtils.makeName(url);
                }

            }
        }).toMain(new Runnable() {
            @Override
            public void run() {

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
