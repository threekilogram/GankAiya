package com.example.wuxio.gankexamples.model.bean;

import java.util.List;

/**
 * 发过干货日期接口
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-09-03
 * @time: 9:43
 */
public class GankHistory {

      private boolean      error;
      private List<String> results;

      public boolean isError ( ) {

            return error;
      }

      public void setError ( boolean error ) {

            this.error = error;
      }

      public List<String> getResults ( ) {

            return results;
      }

      public void setResults ( List<String> results ) {

            this.results = results;
      }
      public boolean isEmpty(){

            return results == null;
      }
}
