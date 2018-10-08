package com.example.wuxio.gankexamples.model.bean;

import java.util.List;

/**
 * @author Liujin 2018-10-08:11:20
 */
public class BeautiesBean {

      private String       mStartDate;
      private List<String> mBeautyUrls;

      public List<String> getBeautyUrls ( ) {

            return mBeautyUrls;
      }

      public void setBeautyUrls ( List<String> beautyUrls ) {

            this.mBeautyUrls = beautyUrls;
      }

      public String getStartDate ( ) {

            return mStartDate;
      }

      public void setStartDate ( String startDate ) {

            mStartDate = startDate;
      }

      @Override
      public String toString ( ) {

            return "BeautiesBean{" +
                "mStartDate='" + mStartDate + '\'' +
                ", mBeautyUrls=" + mBeautyUrls +
                '}';
      }
}
