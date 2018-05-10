package com.example.wuxio.gankexamples.gank.beauty;

import android.util.Pair;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.file.FileNameUtils;
import com.example.wuxio.gankexamples.model.net.RetrofitManger;
import com.example.wuxio.gankexamples.model.net.StreamApi;
import com.example.wuxio.gankexamples.utils.FileIOUtils;

import java.io.File;
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

    private String url;


    public ImageCallable(String url) {

        this.url = url;
    }


    @Override
    public Pair< String, File > call() throws Exception {

        StreamApi streamApi = RetrofitManger.getRetrofit().create(StreamApi.class);
        Call< ResponseBody > stream = streamApi.getStream(url);
        Response< ResponseBody > execute = stream.execute();
        if (execute.isSuccessful()) {

            File picFile = FileManager.getAppPicFile();
            File pic = new File(picFile, FileNameUtils.makeName(url));

            if (pic.exists()) {
                return new Pair<>(url, pic);
            }

            ResponseBody body = execute.body();

            InputStream inputStream = body.byteStream();

            boolean writeIsSuccess = FileIOUtils.writeFileFromIS(pic, inputStream);

            if (writeIsSuccess) {

                return new Pair<>(url, pic);
            } else {

                // TODO: 2018-05-07 write Filed
            }

        } else {

            // TODO: 2018-05-06 execute.failed
        }

        return null;
    }
}
