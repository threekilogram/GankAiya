package com.example.wuxio.gankexamples.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * @author wuxio 2018-05-04:21:44
 */
@Entity
@Uid(2191053166529673684L)
public class ImageBean {

    @Id(assignable = true)
    public long   id;
    public String url;


    @Override
    public String toString() {

        return "ImageBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
