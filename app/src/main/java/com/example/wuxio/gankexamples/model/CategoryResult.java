package com.example.wuxio.gankexamples.model;

import java.util.List;


/**
 * @author wuxio
 */
public class CategoryResult {

    public boolean                  error;
    public List< GankCategoryBean > results;


    @Override
    public String toString() {

        return "CategoryResult{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
