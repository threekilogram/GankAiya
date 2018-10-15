package com.example.wuxio.gankexamples.main.fragment;

import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.Model;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.util.ArrayList;
import tech.threekilogram.depository.cache.json.JsonLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class AndroidModel {

      private static final String TAG = AndroidModel.class.getSimpleName();

      private static JsonLoader<GankCategoryItem> sAndroidLoader;
      private static LocalCategoryBean            sAndroidLocalBean;

      private static final int COUNT = 30;

      public static void init ( ) {

            if( sAndroidLoader == null ) {
                  sAndroidLoader = new JsonLoader<>(
                      COUNT,
                      FileManager.getAndroidFile(),
                      GankCategoryItem.class
                  );
            }

            if( sAndroidLocalBean == null ) {
                  sAndroidLocalBean = new LocalCategoryBean();
                  Log.e( TAG, "init : 初次启动 初始化本地android bean " );
                  buildLocalBean();
            } else {
                  Log.e( TAG, "init : 再次启动 更新本地android bean" );
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  File localBeanFile = FileManager.getLocalAndroidBeanFile();
                  if( localBeanFile.exists() ) {

                        Log.e(
                            TAG, "buildLocalBean : 从本地文件构建 android local bean中 " + localBeanFile );
                        File jsonFile = FileManager.getLatestAndroidJsonFile();
                        sAndroidLocalBean = Model.buildLocalBeanFromFile(
                            GankUrl.ANDROID,
                            localBeanFile,
                            jsonFile
                        );
                        Log.e(
                            TAG,
                            "buildLocalBean : 从本地文件构建 android local bean完成 " + sAndroidLocalBean
                                .getUrls().size()
                        );

                        notifyAllWait();

                        Log.e( TAG, "buildLocalBean : 从最新的json中保存android item 数据中 " );
                        JsonUtil.parserJsonToItemJson( jsonFile, sAndroidLoader );
                        Log.e( TAG, "buildLocalBean : 从最新的json中保存android item 数据完成 " );
                  } else {

                        if( NetWork.hasNetwork() ) {

                              Log.e( TAG, "buildLocalBean : 从网络构建 android local bean中 " );
                              File jsonFile = FileManager.getAndroidJsonFile();
                              sAndroidLocalBean = Model.buildLocalBeanFromNet(
                                  GankUrl.androidAllUrl(),
                                  jsonFile,
                                  localBeanFile
                              );
                              Log.e( TAG, "buildLocalBean : 从网络构建 android local bean完成 "
                                  + sAndroidLocalBean.getUrls().size() );

                              notifyAllWait();

                              Log.e( TAG, "buildLocalBean : 从网络json中保存android item 数据中 " );
                              JsonUtil.parserJsonToItemJson( jsonFile, sAndroidLoader );
                              Log.e( TAG, "buildLocalBean : 从网络json中保存android item 数据完成 " );
                        } else {

                              sAndroidLocalBean = new LocalCategoryBean();
                              sAndroidLocalBean.setUrls( new ArrayList<>() );
                              Log.e( TAG, "buildLocalBean : 没有网络 无法从网络构建android local bean" );
                              notifyAllWait();
                        }
                  }
            } );
      }

      private static void notifyAllWait ( ) {

            synchronized(GankUrl.ANDROID) {
                  GankUrl.ANDROID.notifyAll();
            }
      }

      private static void waitLocalBuild ( ) {

            if( sAndroidLocalBean.getUrls() == null ) {
                  synchronized(GankUrl.ANDROID) {
                        if( sAndroidLocalBean.getUrls() == null ) {
                              try {
                                    GankUrl.ANDROID.wait();
                              } catch(InterruptedException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            }
      }
}
