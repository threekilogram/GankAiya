package com.example.wuxio.gankexamples.dao.category;

import com.example.wuxio.gankexamples.model.ResultsBean;

import java.util.List;

/**
 * @author wuxio 2018-05-05:8:00
 */
public interface CategoryDao {

    /**
     * 插入新数据
     *
     * @param resultsBeans 从网络获取的数据
     */
    void insert(List< ResultsBean > resultsBeans);

    /**
     * 查询所有
     *
     * @return 所有数据
     */
    List< ResultsBean > getAll();

    /**
     * 查询某个类型的数据
     *
     * @param type  类型
     * @param count 查询总数
     * @param page  页数
     * @return 查询结果
     */
    List< ResultsBean > query(String type, int count, int page);
}
