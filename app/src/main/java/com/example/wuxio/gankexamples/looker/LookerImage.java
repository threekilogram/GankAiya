package com.example.wuxio.gankexamples.looker;

import com.example.wuxio.gankexamples.async.Scheduler;
import com.example.wuxio.gankexamples.model.CategoryResult;
import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-01:15:53
 */
public class LookerImage {

    private static final String TAG = "LookerImage";


    public static void getLooker(int count, int page) {

        Scheduler.todo(new BannerLookerAction(5, 1));

    }


    private static class BannerLookerAction implements Runnable {

        private int count;
        private int page;


        public BannerLookerAction(int count, int page) {

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
}
