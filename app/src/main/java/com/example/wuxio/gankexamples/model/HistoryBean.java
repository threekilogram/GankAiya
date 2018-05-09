package com.example.wuxio.gankexamples.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author wuxio 2018-05-09:21:29
 */
@Entity
public class HistoryBean {

    @Id(assignable = true)
    public long date;

}
