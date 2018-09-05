package com.example.wuxio.gankexamples.model;

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

            private List<GankCategory.ResultsBean> Android;
            private List<GankCategory.ResultsBean> App;
            private List<GankCategory.ResultsBean> iOS;
            private List<GankCategory.ResultsBean> 休息视频;
            private List<GankCategory.ResultsBean> 前端;
            private List<GankCategory.ResultsBean> 拓展资源;
            private List<GankCategory.ResultsBean> 瞎推荐;
            private List<GankCategory.ResultsBean> 福利;

            public List<GankCategory.ResultsBean> getAndroid ( ) {

                  return Android;
            }

            public void setAndroid (
                List<GankCategory.ResultsBean> android ) {

                  Android = android;
            }

            public List<GankCategory.ResultsBean> getApp ( ) {

                  return App;
            }

            public void setApp ( List<GankCategory.ResultsBean> app ) {

                  App = app;
            }

            public List<GankCategory.ResultsBean> getiOS ( ) {

                  return iOS;
            }

            public void setiOS ( List<GankCategory.ResultsBean> iOS ) {

                  this.iOS = iOS;
            }

            public List<GankCategory.ResultsBean> get休息视频 ( ) {

                  return 休息视频;
            }

            public void set休息视频 (
                List<GankCategory.ResultsBean> 休息视频 ) {

                  this.休息视频 = 休息视频;
            }

            public List<GankCategory.ResultsBean> get前端 ( ) {

                  return 前端;
            }

            public void set前端 ( List<GankCategory.ResultsBean> 前端 ) {

                  this.前端 = 前端;
            }

            public List<GankCategory.ResultsBean> get拓展资源 ( ) {

                  return 拓展资源;
            }

            public void set拓展资源 (
                List<GankCategory.ResultsBean> 拓展资源 ) {

                  this.拓展资源 = 拓展资源;
            }

            public List<GankCategory.ResultsBean> get瞎推荐 ( ) {

                  return 瞎推荐;
            }

            public void set瞎推荐 ( List<GankCategory.ResultsBean> 瞎推荐 ) {

                  this.瞎推荐 = 瞎推荐;
            }

            public List<GankCategory.ResultsBean> get福利 ( ) {

                  return 福利;
            }

            public void set福利 ( List<GankCategory.ResultsBean> 福利 ) {

                  this.福利 = 福利;
            }

            @Override
            public String toString ( ) {

                  return "ResultsBean{" +
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
