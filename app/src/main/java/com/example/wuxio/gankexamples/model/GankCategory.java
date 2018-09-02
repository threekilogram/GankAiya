package com.example.wuxio.gankexamples.model;

import java.util.List;


/**
 * @author wuxio
 */
public class GankCategory {

    public boolean                  error;
    public List< GankCategoryBean > results;


    @Override
    public String toString() {

        return "GankCategory{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
