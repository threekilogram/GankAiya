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

            private List<GankCategory> Android;
            private List<GankCategory> App;
            private List<GankCategory> iOS;
            private List<GankCategory> 休息视频;
            private List<GankCategory> 前端;
            private List<GankCategory> 拓展资源;
            private List<GankCategory> 瞎推荐;
            private List<GankCategory> 福利;

            public List<GankCategory> getAndroid ( ) {

                  return Android;
            }

            public void setAndroid ( List<GankCategory> android ) {

                  Android = android;
            }

            public List<GankCategory> getApp ( ) {

                  return App;
            }

            public void setApp ( List<GankCategory> app ) {

                  App = app;
            }

            public List<GankCategory> getiOS ( ) {

                  return iOS;
            }

            public void setiOS ( List<GankCategory> iOS ) {

                  this.iOS = iOS;
            }

            public List<GankCategory> get休息视频 ( ) {

                  return 休息视频;
            }

            public void set休息视频 ( List<GankCategory> 休息视频 ) {

                  this.休息视频 = 休息视频;
            }

            public List<GankCategory> get前端 ( ) {

                  return 前端;
            }

            public void set前端 ( List<GankCategory> 前端 ) {

                  this.前端 = 前端;
            }

            public List<GankCategory> get拓展资源 ( ) {

                  return 拓展资源;
            }

            public void set拓展资源 ( List<GankCategory> 拓展资源 ) {

                  this.拓展资源 = 拓展资源;
            }

            public List<GankCategory> get瞎推荐 ( ) {

                  return 瞎推荐;
            }

            public void set瞎推荐 ( List<GankCategory> 瞎推荐 ) {

                  this.瞎推荐 = 瞎推荐;
            }

            public List<GankCategory> get福利 ( ) {

                  return 福利;
            }

            public void set福利 ( List<GankCategory> 福利 ) {

                  this.福利 = 福利;
            }
      }
}
