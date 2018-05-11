package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.banner.adapter.BaseTypePagerAdapter;
import com.example.wuxio.gankexamples.R;

import java.util.List;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {


    protected ViewPager           mViewPager;
    private   List< Bitmap >      mBitmaps;
    private   PicturePagerAdapter mPicturePagerAdapter;


    public static void start(Context context, int dataIndex, int position) {

        Intent starter = new Intent(context, PictureActivity.class);
        context.startActivity(starter);

        PictureManager.getInstance().set(dataIndex, position);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_picture);
        PictureManager.getInstance().register(this);

        initView();
        postAction();
    }


    private void initView() {

        mViewPager = findViewById(R.id.viewPager);
    }


    private void postAction() {

        mViewPager.post(() -> {

            PictureManager instance = PictureManager.getInstance();
            instance.onActivityCreate();
            mBitmaps = instance.getBitmaps();

        });
    }


    public int getBitmapWidth() {

        return mViewPager.getWidth();
    }


    public int getBitmapHeight() {

        return mViewPager.getHeight();
    }


    public void nofityBitmapsChanged(int position) {

        if (mPicturePagerAdapter == null) {

            mPicturePagerAdapter = new PicturePagerAdapter();
            mViewPager.setAdapter(mPicturePagerAdapter);
            mViewPager.addOnPageChangeListener(new PicturePagerOnPageChangeListener());
        }
        mPicturePagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(position);
    }


    @Override
    protected void onDestroy() {

        PictureManager.getInstance().unRegister();
        PictureManager.getInstance().clear();
        mBitmaps = null;
        super.onDestroy();
    }

    //============================ ViewPagerAdapter ============================

    private class PicturePagerAdapter extends BaseTypePagerAdapter {

        private final int NORMAL = 12;
        private final int MORE   = 13;


        @Override
        public int getCount() {

            return mBitmaps.size() + 1;
        }


        @Override
        public int getViewType(int position) {

            if (position == mBitmaps.size()) {

                return MORE;
            } else {
                return NORMAL;
            }
        }


        @Override
        public Object getData(int position, int type) {

            if (type == NORMAL) {
                return mBitmaps.get(position);
            }
            return null;
        }


        @Override
        public View getView(int position, int type) {

            if (type == NORMAL) {
                ImageView imageView = new ImageView(PictureActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                return imageView;
            }

            if (type == MORE) {
                return LayoutInflater.from(PictureActivity.this)
                        .inflate(
                                R.layout.activity_picture_process,
                                mViewPager,
                                false
                        );
            }

            return null;
        }


        @Override
        public void bindData(int position, Object data, View view, int type) {

            if (data instanceof Bitmap) {
                ((ImageView) view).setImageBitmap((Bitmap) data);
            }
        }
    }

    //============================ pager scroll Listener ============================

    private class PicturePagerOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private static final String TAG = "PicturePagerOnPageChang";


        @Override
        public void onPageSelected(int position) {

            if (position == mBitmaps.size()) {
                Log.i(TAG, "onPageSelected:" + "load more");
            }
        }
    }
}
