package com.example.wuxio.gankexamples.model.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * @author wuxio 2018-05-09:21:54
 */
public interface GankHistoryApi {

    /**
     * 获取发过干货日期接口
     *
     * @return 日期
     */
    @Streaming
    @GET("day/history")
    Call< ResponseBody > getHistoryData();

}
