package com.example.wuxio.gankexamples.model.bean;

import java.util.List;

/**
 * 本地缓存bean,用作索引
 *
 * @author Liujin 2018-10-08:11:20
 */
public class LocalCategoryBean {

      private String       mStartDate;
      private List<String> mUrls;

      public List<String> getUrls ( ) {

            return mUrls;
      }

      public void setUrls ( List<String> urls ) {

            this.mUrls = urls;
      }

      public String getStartDate ( ) {

            return mStartDate;
      }

      public void setStartDate ( String startDate ) {

            mStartDate = startDate;
      }

      @Override
      public String toString ( ) {

            return "LocalCategoryBean{" +
                "mStartDate='" + mStartDate + '\'' +
                ", mUrls=" + mUrls +
                '}';
      }
}
