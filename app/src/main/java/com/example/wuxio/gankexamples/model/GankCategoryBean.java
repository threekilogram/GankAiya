package com.example.wuxio.gankexamples.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

/**
 * @author wuxio 2018-05-04:21:25
 */

@Entity
public class GankCategoryBean {

    @Id(assignable = true)
    public long                id;
    public String              _id;
    public long                createdAt;
    public String              desc;
    public long                publishedAt;
    public String              source;
    @Index
    public String              type;
    public String              url;
    public boolean             used;
    public String              who;
    public ToMany< ImageBean > images;


    @Override
    public String toString() {

        return "GankCategoryBean{" +
                "id=" + id +
                ", _id='" + _id + '\'' +
                ", createdAt=" + createdAt +
                ", desc='" + desc + '\'' +
                ", publishedAt=" + publishedAt +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", who='" + who + '\'' +
                ", images=" + images +
                '}';
    }
}

