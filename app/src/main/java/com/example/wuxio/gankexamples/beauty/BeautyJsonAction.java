package com.example.wuxio.gankexamples.beauty;

import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.io.Reader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio
 */
public class BeautyJsonAction implements Runnable {

    private static final String TAG = "BeautyJsonAction";

    private int count;
    private int page;


    public BeautyJsonAction(int count, int page) {

        this.count = count;
        this.page = page;
    }


    @Override
    public void run() {

        Call< ResponseBody > categoryData = NetWork.gankApi().getCategoryData("福利", count, page);

        try {
            Response< ResponseBody > gankResponse = categoryData.execute();
            if (gankResponse.isSuccessful()) {
                Reader charStream = gankResponse.body().charStream();

            } else {

                // TODO: 2018-05-01 获取数据失败 response failed

            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO: 2018-05-01 problem to connect to server
        }
    }
}