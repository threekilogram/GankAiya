package com.example.wuxio.gankexamples.action;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;

import java.io.File;

/**
 * @author wuxio 2018-05-11:9:18
 */
public class DecodeSampledBitmapAction {

    public static Bitmap decode(File file, int width, int height, int res) {

        try {

            return BitmapReader.decodeSampledBitmap(file, width, height);

        } catch (Exception e) {
            e.printStackTrace();

            /* 加载失败之后,加载默认失败图片 */

            return BitmapFactory.decodeResource(App.INSTANCE.getResources(), res);
        }
    }

}
