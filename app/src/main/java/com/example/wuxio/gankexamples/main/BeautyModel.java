package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.Model;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import com.threekilogram.objectbus.executor.MainExecutor;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateValue;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class BeautyModel {

      private static final String TAG = BeautyModel.class.getSimpleName();

      private static WeakReference<MainActivity> sRef;

      private static LocalCategoryBean sLocalBeautyBean;

      /**
       * 初始化
       */
      public static void init ( ) {

            if( sLocalBeautyBean == null ) {
                  sLocalBeautyBean = new LocalCategoryBean();
                  buildLocalBean();
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  /* 1.读取本地 LocalCategoryBean 缓存*/
                  File localBeanFile = FileManager.getLocalBeautyBeanFile();
                  if( localBeanFile.exists() ) {

                        sLocalBeautyBean = Model.buildLocalBeanFromFile(
                            GankUrl.BEAUTY,
                            localBeanFile,
                            FileManager.getLatestBeautyJsonFile()
                        );

                        /* 唤醒等待beautiesBean创建的线程启动 */
                        notifyAllWait();
                  } else {

                        /* 2.没有beauty历史记录缓存 */
                        if( NetWork.hasNetwork() ) {

                              /* 3.从网络下载,并构建bean*/
                              sLocalBeautyBean = Model.buildLocalBeanFromNet(
                                  GankUrl.beautyAllUrl(),
                                  FileManager.getBeautyJsonFile(),
                                  localBeanFile
                              );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                        } else {

                              /* 3.如果无法从网络构建 */
                              sLocalBeautyBean = new LocalCategoryBean();
                              sLocalBeautyBean.setUrls( new ArrayList<>() );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                              Log.e( TAG, "buildLocalBean : 没有网络,无法获取历史福利数据" );
                        }
                  }

                  cacheBeautyPicture();
            } );
      }

      private static void notifyAllWait ( ) {

            synchronized(GankUrl.BEAUTY) {
                  GankUrl.BEAUTY.notifyAll();
            }
      }

      private static void waitLocalBuild ( ) {

            if( sLocalBeautyBean.getUrls() == null ) {
                  synchronized(GankUrl.BEAUTY) {
                        if( sLocalBeautyBean.getUrls() == null ) {
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
                  for( int i = 0; i < beautyUrls.size(); i++ ) {
                        if( NetStateChangeManager.getCurrentNetState()
                            == NetStateValue.ONLY_WIFI_CONNECT ) {

                              String url = beautyUrls.get( i );
                              BitmapCache.downLoadPicture( url );
                        }
                  }
            } );
      }

      /**
       * 获取构建好的bean
       */
      public static LocalCategoryBean getLocalBeautyBean ( ) {

            waitLocalBuild();
            return sLocalBeautyBean;
      }

      /**
       * 获取构建好的bean中的image Urls
       */
      public static List<String> getUrls ( ) {

            waitLocalBuild();
            return sLocalBeautyBean.getUrls();
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
                  List<Bitmap> bitmaps = BitmapCache.loadListBitmaps( needUrls );

                  for( Bitmap bitmap : bitmaps ) {
                        if( bitmap == null ) {
                              if( NetWork.hasNetwork() ) {
                                    ToastMessage.toast( "没有网络" );
                                    Log.e( TAG, "loadBannerBitmap : 没有网络" );
                              } else {
                                    ToastMessage.toast( "无法获取banner图片数据" );
                                    Log.e( TAG, "loadBannerBitmap : 无法获取banner图片数据" );
                              }
                        }
                  }

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
