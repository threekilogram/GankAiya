package com.example.wuxio.gankexamples.gank.beauty;

import android.util.Log;
import android.util.Pair;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
import com.example.wuxio.gankexamples.model.net.RetrofitManger;
import com.example.wuxio.gankexamples.model.net.StreamApi;
import com.example.wuxio.gankexamples.utils.FileIOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-06:20:33
 */
public class ImageCallable implements Callable< Pair< String, File > > {

    private static final String TAG = "ImageCallable";

    private String mUrl;


    public ImageCallable(String url) {

        this.mUrl = url;
    }


    @Override
    public Pair< String, File > call() {

        String url = mUrl;

        File picFile = FileManager.getAppPicFile();
        File pic = new File(picFile, FileNameUtils.makeName(url));

        if (pic.exists()) {
            return new Pair<>(url, pic);
        } else {

            /* 创建文件 */

            boolean mkdirs = pic.getParentFile().mkdirs();
            if (mkdirs) {
                boolean newFile = false;
                try {
                    newFile = pic.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "call: createNewFile Failed");
                }
                if (!newFile) {
                    Log.e(TAG, "call: createNewFile Failed");
                }
            } else {
                Log.e(TAG, "call: mkDirs failed");
            }
        }

        try {

            /* 创建图片流,保存到本地 */

            StreamApi streamApi = RetrofitManger.getRetrofit().create(StreamApi.class);
            Call< ResponseBody > stream = streamApi.getStream(url);
            Response< ResponseBody > execute = stream.execute();

            if (execute.isSuccessful()) {

                ResponseBody body = execute.body();
                assert body != null;
                InputStream inputStream = body.byteStream();

                boolean writeIsSuccess = FileIOUtils.writeFileFromIS(pic, inputStream);

                if (writeIsSuccess) {

                    return new Pair<>(url, pic);
                } else {

                    Log.e(TAG, "call: write picture failed");
                }

            } else {

                Log.e(TAG, "call: code not in 200~300");
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, "call: network IOException");
        }

        return null;
    }
}
