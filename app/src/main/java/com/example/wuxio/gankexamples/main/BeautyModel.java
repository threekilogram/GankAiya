package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.BitmapManager;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.Model;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.executor.MainExecutor;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateValue;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class BeautyModel {

      private static WeakReference<MainActivity> sRef;
      private static LocalCategoryBean           sBeautyLocalBean;

      /**
       * 初始化
       */
      public static void init ( ) {

            if( sBeautyLocalBean == null ) {
                  sBeautyLocalBean = new LocalCategoryBean();
                  buildLocalBean();
            } else {
                  /* 更新一下 */
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  File localBeanFile = FileManager.getLocalBeautyBeanFile();
                  if( localBeanFile.exists() ) {

                        sBeautyLocalBean = Model.buildLocalBeanFromFile(
                            GankUrl.BEAUTY,
                            localBeanFile,
                            FileManager.getLatestBeautyJsonFile()
                        );
                        notifyAllWait();
                  } else {

                        if( NetWork.hasNetwork() ) {

                              sBeautyLocalBean = Model.buildLocalBeanFromNet(
                                  GankUrl.beautyAllUrl(),
                                  FileManager.getBeautyJsonFile(),
                                  localBeanFile
                              );
                              notifyAllWait();
                        } else {

                              sBeautyLocalBean = new LocalCategoryBean();
                              sBeautyLocalBean.setUrls( new ArrayList<>() );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                        }
                  }
            } );
      }

      /**
       * 唤醒{@link #waitLocalBuild()}等待的线程
       */
      private static void notifyAllWait ( ) {

            synchronized(GankUrl.BEAUTY) {
                  GankUrl.BEAUTY.notifyAll();
            }
      }

      /**
       * 如果{@link #sBeautyLocalBean}没有构建完成那么等待完成
       */
      private static void waitLocalBuild ( ) {

            /* 双重加锁验证 */
            if( sBeautyLocalBean.getUrls() == null ) {
                  synchronized(GankUrl.BEAUTY) {
                        if( sBeautyLocalBean.getUrls() == null ) {
                              try {
                                    GankUrl.BEAUTY.wait();
                              } catch(InterruptedException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            }
      }

      /**
       * 缓存福利图片文件
       */
      private static void cacheBeautyPicture ( ) {

            List<String> beautyUrls = getUrls();
            PoolExecutor.execute( ( ) -> {

                  int success = 0;
                  int failed = 0;
                  for( int i = 0; i < beautyUrls.size(); i++ ) {
                        if( NetStateChangeManager.getCurrentNetState()
                            >= NetStateValue.ONLY_WIFI_CONNECT ) {

                              String url = beautyUrls.get( i );
                              try {
                                    if( url != null ) {
                                          File file = BitmapManager.downLoadPicture( url );
                                          if( file.exists() ) {
                                                success++;
                                          } else {
                                                failed++;
                                          }
                                    }
                              } catch(Exception e) {
                                    /*  */
                              }
                        }
                  }
            } );
      }

      /**
       * 获取构建好的bean
       */
      public static LocalCategoryBean getBeautyLocalBean ( ) {

            waitLocalBuild();
            return sBeautyLocalBean;
      }

      /**
       * 获取构建好的bean中的image Urls
       */
      public static List<String> getUrls ( ) {

            waitLocalBuild();
            return sBeautyLocalBean.getUrls();
      }

      /**
       * 绑定宿主
       */
      static void bind ( MainActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      /**
       * 为{@link MainActivity}获取banner图片
       */
      static void loadBannerBitmap ( ) {

            PoolExecutor.execute( ( ) -> {

                  List<String> beautiesUrl = getUrls();

                  int startIndex = 0;
                  int count = 5;
                  ArrayList<String> needUrls = new ArrayList<>( count );
                  for( int i = startIndex; i < startIndex + count; i++ ) {

                        needUrls.add( beautiesUrl.get( i ) );
                  }

                  List<Bitmap> bitmaps = BitmapManager.loadListBitmaps( needUrls );
                  setMainActivityBannerData( 0, bitmaps );
            } );
      }

      private static void setMainActivityBannerData ( int startIndex, List<Bitmap> bitmaps ) {

            MainExecutor.execute( ( ) -> {
                  try {
                        sRef.get().onBannerBitmapsPrepared( startIndex, bitmaps );
                  } catch(Exception e) {
                        /* nothing to worry about */
                  }
            } );
      }
}
