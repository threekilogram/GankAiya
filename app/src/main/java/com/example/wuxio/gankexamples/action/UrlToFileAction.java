package com.example.wuxio.gankexamples.action;

import android.util.Log;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
import com.example.wuxio.gankexamples.retrofit.RetrofitManger;
import com.example.wuxio.gankexamples.retrofit.StreamApi;
import com.example.wuxio.gankexamples.utils.FileIOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-11:9:02
 */
public class UrlToFileAction {

    private static final String TAG = "UrlToFileAction";


    public static File loadUrlToFile(String url) {

        File picFile = FileManager.getAppPicFile();
        File pic = new File(picFile, FileNameUtils.makeName(url));

        if (pic.exists()) {

            return pic;
        } else {

            /* 创建文件 */
            try {
                pic.getParentFile().mkdirs();
                pic.createNewFile();
            } catch (IOException e) {

                Log.e(TAG, "create pic file failed");
                return null;
            }
        }

        try {

            /* 创建图片流 */

            StreamApi streamApi = RetrofitManger.getRetrofit().create(StreamApi.class);
            Call< ResponseBody > stream = streamApi.getStream(url);
            Response< ResponseBody > execute = stream.execute();

            if (execute.isSuccessful()) {

                ResponseBody body = execute.body();
                assert body != null;
                InputStream inputStream = body.byteStream();

                /* 保存到本地 */
                try {
                    FileIOUtils.writeFileFromIS(pic, inputStream);
                    return pic;
                } catch (Exception e) {
                    Log.e(TAG, " write picture to local failed");
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
