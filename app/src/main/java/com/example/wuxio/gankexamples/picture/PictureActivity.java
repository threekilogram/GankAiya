package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.wuxio.gankexamples.R;
import com.threekilogram.banner.adapter.BaseTypePagerAdapter;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {


    protected ViewPager           mViewPager;
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
            instance.onStart();
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
        if (position > 0) {
            mViewPager.setCurrentItem(position);
        }
    }


    @Override
    protected void onDestroy() {

        PictureManager.getInstance().unRegister();
        PictureManager.getInstance().clear();
        super.onDestroy();
    }

    //============================ ViewPagerAdapter ============================

    private class PicturePagerAdapter extends BaseTypePagerAdapter {

        private final int NORMAL = 12;
        private final int MORE   = 13;


        @Override
        public int getCount() {

            return PictureManager.getInstance().getItemCount() + 1;
        }


        @Override
        public int getViewType(int position) {

            if (position == getCount() - 1) {

                return MORE;
            } else {
                return NORMAL;
            }
        }


        @Override
        public Object getData(int position, int type) {

            if (type == NORMAL) {
                return PictureManager.getInstance().loadBitmap(position);
            }
            return null;
        }


        @Override
        public View getView(ViewGroup container, int position, int type) {

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


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            super.destroyItem(container, position, object);

            PictureManager.getInstance().releaseBitmap(position);
        }
    }

    //============================ pager scroll Listener ============================

    private class PicturePagerOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private static final String TAG = "PicturePagerOnPageChang";


        @Override
        public void onPageSelected(int position) {

            if (position == mPicturePagerAdapter.getCount() - 1) {
                PictureManager.getInstance().loadMore();
            }
        }
    }
}
