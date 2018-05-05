package com.example.wuxio.gankexamples.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author wuxio 2018-05-04:21:44
 */
@Entity
public class ImageBean {

    @Id(assignable = true)
    public long   id;
    public String url;
    public String path;


    @Override
    public String toString() {

        return "ImageBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
