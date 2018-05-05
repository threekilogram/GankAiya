package com.example.wuxio.gankexamples.dao;

import com.example.wuxio.gankexamples.model.ResultsBean;

import java.util.List;

/**
 * @author wuxio 2018-05-05:8:00
 */
public interface CategoryDao {

    /**
     * 插入新数据
     * @param resultsBeans 从网络获取的数据
     */
    void insert(List<ResultsBean> resultsBeans);

    List< ResultsBean > getAll();
}
