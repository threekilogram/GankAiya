package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.banner.adapter.BasePagerAdapter;
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
            mPicturePagerAdapter = new PicturePagerAdapter();
            mViewPager.setAdapter(mPicturePagerAdapter);
        });
    }


    public int getBitmapWidth() {

        return mViewPager.getWidth();
    }


    public int getBitmapHeight() {

        return mViewPager.getHeight();
    }


    public void nofityBitmapsChanged(int position) {

        if (mPicturePagerAdapter != null) {
            mViewPager.setCurrentItem(position);
            mPicturePagerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {

        PictureManager.getInstance().unRegister();
        PictureManager.getInstance().clear();
        mBitmaps = null;
        super.onDestroy();
    }

    //============================ ViewPagerAdapter ============================

    private class PicturePagerAdapter extends BasePagerAdapter< Bitmap, ImageView > {

        @Override
        public int getCount() {

            return mBitmaps.size();
        }


        @Override
        public Bitmap getData(int i) {

            try {
                return mBitmaps.get(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        public ImageView getView(int i) {

            ImageView imageView = new ImageView(PictureActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            return imageView;
        }


        @Override
        public void bindData(int i, Bitmap bitmap, ImageView imageView) {

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
