package com.example.wuxio.gankexamples.beauty;

import com.example.wuxio.gankexamples.beauty.model.BeautyEntity;
import com.example.wuxio.gankexamples.beauty.model.Convert;
import com.example.wuxio.gankexamples.model.CategoryResult;
import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.util.List;

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

        Call< CategoryResult > categoryData = NetWork.gankApi().getCategoryData("福利", count, page);

        try {
            Response< CategoryResult > gankResponse = categoryData.execute();
            if (gankResponse.isSuccessful()) {
                CategoryResult body = gankResponse.body();

                if (body != null && !body.error) {
                    List< CategoryResult.ResultsBean > results = body.results;

                    List< BeautyEntity > entities = Convert.map(results);
                    BeautyManager.getInstance().notifyNew(entities);

                } else {

                    // TODO: 2018-05-01 body null or body is error
                }

            } else {

                // TODO: 2018-05-01 获取数据失败 response failed

            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO: 2018-05-01 problem to connect to server
        }
    }
}