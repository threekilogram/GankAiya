package com.example.wuxio.gankexamples.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wuxio 2018-04-29:23:24
 */
public class NetWork {


    private static GankApi  gankApi;
    private static Retrofit retrofitForRx;
    private static final String BASE_URL = "http://gank.io/api/";

    private static StreamApi streamApi;


    private static Retrofit getRetrofit() {

        if (retrofitForRx == null) {
            retrofitForRx = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitForRx;
    }


    public static GankApi gankApi() {

        if (gankApi == null) {

            gankApi = getRetrofit().create(GankApi.class);
        }

        return gankApi;
    }


    public static StreamApi streamApi() {

        if (streamApi == null) {

            streamApi = getRetrofit().create(StreamApi.class);
        }

        return streamApi;
    }
}
