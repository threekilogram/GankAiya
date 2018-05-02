package com.example.wuxio.gankexamples.beauty.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author wuxio 2018-05-02:15:39
 */
@Entity
public class BeautyEntity {

    @Id(assignable = true)
    public long   publishDate;
    public String url;
    public String path;


    public BeautyEntity(long publishDate, String url) {

        this.publishDate = publishDate;
        this.url = url;
    }


    public long getPublishDate() {

        return publishDate;
    }


    public void setPublishDate(long publishDate) {

        this.publishDate = publishDate;
    }


    public String getUrl() {

        return url;
    }


    public void setUrl(String url) {

        this.url = url;
    }


    public String getPath() {

        return path;
    }


    public void setPath(String path) {

        this.path = path;
    }


    @Override
    public String toString() {

        return "BeautyEntity{" +
                "publishDate=" + publishDate +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
