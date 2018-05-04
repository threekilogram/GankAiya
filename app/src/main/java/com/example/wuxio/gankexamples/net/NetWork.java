package com.example.wuxio.gankexamples.net;

import retrofit2.Retrofit;

/**
 * @author wuxio 2018-04-29:23:24
 */
public class NetWork {


    private static GankApi  gankApi;
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://gank.io/api/";


    private static Retrofit getRetrofit() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
        }

        return retrofit;
    }


    public static GankApi gankApi() {

        if (gankApi == null) {

            gankApi = getRetrofit().create(GankApi.class);
        }

        return gankApi;
    }
}
