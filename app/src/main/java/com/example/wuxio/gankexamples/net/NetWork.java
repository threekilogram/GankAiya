package com.example.wuxio.gankexamples.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wuxio 2018-04-29:23:24
 */
public class NetWork {


    private static GankApi gankApi;
    private static final String BASE_URL = "http://gank.io/api/";


    public static GankApi gankApi() {

        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }

        return gankApi;
    }
}
