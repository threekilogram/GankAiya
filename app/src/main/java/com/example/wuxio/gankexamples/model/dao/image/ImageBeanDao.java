package com.example.wuxio.gankexamples.model.dao.image;

import com.example.wuxio.gankexamples.model.ImageBean;

import java.util.List;

/**
 * @author wuxio 2018-05-05:10:44
 */
public interface ImageBeanDao {

    /**
     * query url,s imageBean
     *
     * @param url url
     * @return imageBean
     */
    ImageBean query(String url);

    /**
     * query
     *
     * @param urls urls
     * @return beans
     */
    List< ImageBean > query(List< String > urls);

    /**
     * 插入
     *
     * @param urls urls
     */
    void insert(List< String > urls);
}
