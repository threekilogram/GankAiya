package com.example.wuxio.gankexamples.model;

import java.util.List;

/**
 * @author wuxio 2018-05-05:8:20
 */
public class GankDateApi {
    

    private boolean        error;
    private List< String > results;


    public boolean isError() {

        return error;
    }


    public void setError(boolean error) {

        this.error = error;
    }


    public List< String > getResults() {

        return results;
    }


    public void setResults(List< String > results) {

        this.results = results;
    }
}
