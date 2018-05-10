package com.example.wuxio.gankexamples.model.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author wuxio 2018-04-29:23:01
 */
public interface StreamApi {


    /**
     * 如果下载一个非常大的文件，Retrofit会试图将整个文件读进内存。为了避免这种现象的发生，
     * 我们添加了一个特殊的注解{@link Streaming}来声明请求。
     *
     * @param path 路径
     * @return call data
     */
    @Streaming
    @GET
    Call< ResponseBody > getStream(@Url String path);
}
