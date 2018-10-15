package com.example.wuxio.gankexamples.main.fragment;

import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
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
                  buildLocalBean();
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  /* 1.读取本地 LocalCategoryBean 缓存*/
                  File localBeanFile = FileManager.getLocalAndroidBeanFile();
                  if( localBeanFile.exists() ) {

                        sAndroidLocalBean = Model.buildLocalBeanFromFile(
                            GankUrl.ANDROID,
                            localBeanFile,
                            FileManager.getLatestAndroidJsonFile()
                        );

                        /* 唤醒等待beautiesBean创建的线程启动 */
                        notifyAllWait();
                  } else {

                        /* 2.没有beauty历史记录缓存 */
                        if( NetWork.hasNetwork() ) {

                              /* 3.从网络下载,并构建bean*/
                              sAndroidLocalBean = Model.buildLocalBeanFromNet(
                                  GankUrl.androidAllUrl(),
                                  FileManager.getAndroidJsonFile(),
                                  localBeanFile
                              );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                        } else {

                              /* 3.如果无法从网络构建 */
                              sAndroidLocalBean = new LocalCategoryBean();
                              sAndroidLocalBean.setUrls( new ArrayList<>() );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                              Log.e( TAG, "buildLocalBean : 没有网络,无法获取历史福利数据" );
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
