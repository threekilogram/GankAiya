package com.example.wuxio.gankexamples.model.bean;

import java.util.List;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-09-03
 * @time: 14:14
 */
public class GankDay {

      private boolean      error;
      private ResultsBean  results;
      private List<String> category;

      public boolean isError ( ) { return error;}

      public void setError ( boolean error ) { this.error = error;}

      public ResultsBean getResults ( ) { return results;}

      public void setResults ( ResultsBean results ) { this.results = results;}

      public List<String> getCategory ( ) { return category;}

      public void setCategory ( List<String> category ) { this.category = category;}

      public static class ResultsBean {

            private List<GankCategoryItem> Android;
            private List<GankCategoryItem> App;
            private List<GankCategoryItem> iOS;
            private List<GankCategoryItem> 休息视频;
            private List<GankCategoryItem> 前端;
            private List<GankCategoryItem> 拓展资源;
            private List<GankCategoryItem> 瞎推荐;
            private List<GankCategoryItem> 福利;

            public List<GankCategoryItem> getAndroid ( ) {

                  return Android;
            }

            public void setAndroid ( List<GankCategoryItem> android ) {

                  Android = android;
            }

            public List<GankCategoryItem> getApp ( ) {

                  return App;
            }

            public void setApp ( List<GankCategoryItem> app ) {

                  App = app;
            }

            public List<GankCategoryItem> getiOS ( ) {

                  return iOS;
            }

            public void setiOS ( List<GankCategoryItem> iOS ) {

                  this.iOS = iOS;
            }

            public List<GankCategoryItem> get休息视频 ( ) {

                  return 休息视频;
            }

            public void set休息视频 ( List<GankCategoryItem> 休息视频 ) {

                  this.休息视频 = 休息视频;
            }

            public List<GankCategoryItem> get前端 ( ) {

                  return 前端;
            }

            public void set前端 ( List<GankCategoryItem> 前端 ) {

                  this.前端 = 前端;
            }

            public List<GankCategoryItem> get拓展资源 ( ) {

                  return 拓展资源;
            }

            public void set拓展资源 ( List<GankCategoryItem> 拓展资源 ) {

                  this.拓展资源 = 拓展资源;
            }

            public List<GankCategoryItem> get瞎推荐 ( ) {

                  return 瞎推荐;
            }

            public void set瞎推荐 ( List<GankCategoryItem> 瞎推荐 ) {

                  this.瞎推荐 = 瞎推荐;
            }

            public List<GankCategoryItem> get福利 ( ) {

                  return 福利;
            }

            public void set福利 ( List<GankCategoryItem> 福利 ) {

                  this.福利 = 福利;
            }

            @Override
            public String toString ( ) {

                  return "GankCategoryItem{" +
                      "Android=" + Android +
                      ", App=" + App +
                      ", iOS=" + iOS +
                      ", 休息视频=" + 休息视频 +
                      ", 前端=" + 前端 +
                      ", 拓展资源=" + 拓展资源 +
                      ", 瞎推荐=" + 瞎推荐 +
                      ", 福利=" + 福利 +
                      '}';
            }
      }

      @Override
      public String toString ( ) {

            return "GankDay{" +
                "error=" + error +
                ", results=" + results +
                ", category=" + category +
                '}';
      }
}
