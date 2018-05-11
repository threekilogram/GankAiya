package com.example.wuxio.gankexamples.picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.wuxio.gankexamples.R;

/**
 * @author wuxio
 */
public class PictureActivity extends AppCompatActivity {


    protected ImageView mImageView;


    public static void start(Context context, int dataIndex, int position) {

        Intent starter = new Intent(context, PictureActivity.class);
        context.startActivity(starter);

        PictureManager.getInstance().set(dataIndex, position);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_picture);
        initView();
        PictureManager.getInstance().register(this);
    }


    private void initView() {

        mImageView = findViewById(R.id.imageView);
        mImageView.post(() -> PictureManager.getInstance().onActivityCreate());
    }


    public ImageView getImageView() {

        return mImageView;
    }


    @Override
    protected void onDestroy() {

        PictureManager.getInstance().unRegister();
        PictureManager.getInstance().clear();
        super.onDestroy();
    }
}
