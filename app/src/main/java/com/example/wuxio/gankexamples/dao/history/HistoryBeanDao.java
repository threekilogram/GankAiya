package com.example.wuxio.gankexamples.dao.history;

import com.example.wuxio.gankexamples.model.HistoryBean;

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
    HistoryBean query(long date);

    /**
     * query
     *
     * @param dates date
     * @return beans
     */
    List< HistoryBean > query(long[] dates);

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
    void insert(List< HistoryBean > historyBeans);

}
