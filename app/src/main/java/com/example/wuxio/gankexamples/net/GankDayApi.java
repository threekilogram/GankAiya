package com.example.wuxio.gankexamples.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * @author wuxio 2018-05-10:7:49
 */
public interface GankDayApi {


    /**
     * 获取发过干货日期接口
     *
     * @param year  year
     * @param month month
     * @param day   day
     * @return body
     */
    @Streaming
    @GET("day/{year}/{month}/{day}")
    Call< ResponseBody > getHistoryData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day
    );

}
