package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.widget.ImageView;

import com.example.wuxio.gankexamples.R;

import java.io.File;
import java.util.List;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {


    protected ImageView mImageView;


    public static void start(Context context,
                             int position,
                             List< String > urls,
                             ArrayMap< String, File > bitmapFileMap) {

        Intent starter = new Intent(context, PictureActivity.class);
        context.startActivity(starter);
        PictureManager.getInstance().set(position, urls, bitmapFileMap);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_picture);
        initView();
        PictureManager.getInstance().register(this);
    }


    private void initView() {

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.post(new Runnable() {
            @Override
            public void run() {

                PictureManager.getInstance().onActivityCreate();
            }
        });
    }


    public ImageView getImageView() {

        return mImageView;
    }


    @Override
    protected void onDestroy() {

        PictureManager.getInstance().unRegister();
        super.onDestroy();
    }
}
