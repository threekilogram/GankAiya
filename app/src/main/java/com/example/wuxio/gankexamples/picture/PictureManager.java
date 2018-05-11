package com.example.wuxio.gankexamples.picture;

import android.graphics.Bitmap;

import com.example.wuxio.gankexamples.BaseActivityManager;

import java.util.List;

/**
 * @author wuxio 2018-05-07:14:44
 */
public class PictureManager extends BaseActivityManager< PictureActivity > {

    private int            mBannerPosition;
    private int            mDataIndex;
    private List< Bitmap > mBitmaps;

    //============================ data ============================


    public void set(int dataIndex, int position) {

        mBannerPosition = position;
        mDataIndex = dataIndex;
    }


    public void clear() {

        mBannerPosition = 0;
        mBitmaps = null;
    }

    //============================ core ============================


    @Override
    public void onActivityCreate() {

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
