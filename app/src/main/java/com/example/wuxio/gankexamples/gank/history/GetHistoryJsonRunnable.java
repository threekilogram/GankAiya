package com.example.wuxio.gankexamples.gank.history;

import com.example.wuxio.gankexamples.model.HistoryBean;
import com.example.wuxio.gankexamples.model.HistoryBeanParser;
import com.example.wuxio.gankexamples.model.net.GankHistoryApi;
import com.example.wuxio.gankexamples.model.net.RetrofitManger;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-10:6:48
 */
public class GetHistoryJsonRunnable implements Runnable {

    List< HistoryBean > mHistoryBeans;


    public List< HistoryBean > getHistoryBeans() {

        return mHistoryBeans;
    }


    @Override
    public void run() {

        GankHistoryApi historyApi = RetrofitManger.historyApi();
        Call< ResponseBody > historyData = historyApi.getHistoryData();

        try {
            Response< ResponseBody > execute = historyData.execute();
            if (execute.isSuccessful()) {

                Reader reader = execute.body().charStream();
                List< HistoryBean > historyBeans = HistoryBeanParser.parse(reader);

                mHistoryBeans = historyBeans;

            } else {

                // TODO: 2018-05-09 failed

            }

        } catch (IOException e) {
            e.printStackTrace();

            // TODO: 2018-05-09 IOException
        }

    }
}
