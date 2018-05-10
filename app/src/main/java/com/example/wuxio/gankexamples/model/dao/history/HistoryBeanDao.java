package com.example.wuxio.gankexamples.model.dao.history;

import com.example.wuxio.gankexamples.model.GankHistoryBean;

import java.util.List;

/**
 * @author wuxio 2018-05-10:6:23
 */
public interface HistoryBeanDao {

    /**
     * query url,s imageBean
     *
     * @param date date
     * @return imageBean
     */
    GankHistoryBean query(long date);

    /**
     * query
     *
     * @param dates date
     * @return beans
     */
    List< GankHistoryBean > query(long[] dates);

    /**
     * 最新的
     *
     * @return 最新的
     */
    long findLatest();

    /**
     * 插入
     *
     * @param historyBeans beans
     */
    void insert(List< GankHistoryBean > historyBeans);

}
