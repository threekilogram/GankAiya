package com.example.wuxio.gankexamples.action;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
import com.example.wuxio.gankexamples.utils.image.BitmapReader;
import java.io.File;

/**
 * @author wuxio 2018-05-11:8:54
 */
public class UrlToBitmapAction {

    public static Bitmap loadUrlToBitmap(String url, int width, int height) {

        File file = getPicFile(url);

        /* load Picture to Disk */

        if (!file.exists()) {

            UrlToFileAction.loadUrlToFile(url);
        }

        /* decode bitmap */
        /* try catch because file maybe null,if null use a default bitmap */
        try {

            return BitmapReader.decodeSampledBitmap(file, width, height);
        } catch (Exception e) {

            return BitmapFactory.decodeResource(App.INSTANCE.getResources(), R.drawable.nothing);
        }
    }


    private static File getPicFile(String url) {

        String name = FileNameUtils.makeName(url);
        File file = FileManager.getAppPicFile();
        return new File(file, name);
    }
}
