package com.example.wuxio.gankexamples.net;

import com.example.wuxio.gankexamples.model.CategoryResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author wuxio 2018-04-29:23:01
 */
public interface GankApi {


    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     *
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页： 数字，大于0
     *
     * @param category 数据类型
     * @param count    请求个数
     * @param page     第几页
     * @return Observable<    CategoryResult    >
     */
    @GET("data/{category}/{count}/{page}")
    Observable< CategoryResult > getCategoryData(@Path("category") String category, @Path("count") int
            count, @Path("page") int page);
}
