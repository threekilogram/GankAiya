package com.example.wuxio.gankexamples.retrofit;

import com.example.wuxio.gankexamples.model.net.GankCategoryApi;

import retrofit2.Retrofit;

/**
 * @author wuxio 2018-04-29:23:24
 */
public class RetrofitManger {


    private static GankCategoryApi sGankCategoryApi;
    private static Retrofit        retrofit;
    private static final String BASE_URL = "http://gank.io/api/";


    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
        }

        return retrofit;
    }


    /**
     * @return 分类数据
     */
    public static GankCategoryApi categoryApi() {

        if (sGankCategoryApi == null) {

            sGankCategoryApi = getRetrofit().create(GankCategoryApi.class);
        }

        return sGankCategoryApi;
    }
}
