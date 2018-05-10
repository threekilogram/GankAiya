package com.example.wuxio.gankexamples.model.net;

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


    /**
     * @return 发过日期的Api
     */
    public static GankHistoryApi historyApi() {

        return getRetrofit().create(GankHistoryApi.class);
    }


    /**
     * @return 发过日期的Api
     */
    public static GankDayApi dayApi() {

        return getRetrofit().create(GankDayApi.class);
    }
}
