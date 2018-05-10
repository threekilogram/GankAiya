package com.example.wuxio.gankexamples.model;

import java.util.List;

/**
 * @author wuxio 2018-05-04:21:25
 */
public class GankCategoryBean {

    public String         _id;
    public long           createdAt;
    public String         desc;
    public long           publishedAt;
    public String         source;
    public String         type;
    public String         url;
    public boolean        used;
    public String         who;
    public List< String > images;


    @Override
    public String toString() {

        return "GankCategoryBean{" +
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

