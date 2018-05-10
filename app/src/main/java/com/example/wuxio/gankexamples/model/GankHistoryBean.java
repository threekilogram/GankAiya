package com.example.wuxio.gankexamples.model;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * @author wuxio 2018-05-09:21:29
 */
@Entity
public class GankHistoryBean {

    @Id(assignable = true)
    public long date;

    public ToOne< GankDayContentBean > dayContent;


    @Override
    public String toString() {

        Date dateTime = new Date(date);

        return dateTime.toString();
    }
}
