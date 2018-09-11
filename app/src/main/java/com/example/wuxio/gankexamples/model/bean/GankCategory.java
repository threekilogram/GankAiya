package com.example.wuxio.gankexamples.model.bean;

import java.util.List;

/**
 * @author wuxio
 */
public class GankCategory {

      private boolean                error;
      private List<GankCategoryItem> results;

      public boolean isError ( ) { return error;}

      public void setError ( boolean error ) { this.error = error;}

      public List<GankCategoryItem> getResults ( ) { return results;}

      public void setResults ( List<GankCategoryItem> results ) { this.results = results;}

      @Override
      public String toString ( ) {

            return "GankCategory{" +
                "error=" + error +
                ", results=" + results +
                '}';
      }
}
