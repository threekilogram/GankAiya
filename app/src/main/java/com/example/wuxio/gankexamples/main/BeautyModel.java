package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.DateUtil;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.example.wuxio.gankexamples.utils.ToastMessage;
import com.threekilogram.objectbus.executor.MainExecutor;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tech.threekilogram.depository.cache.json.ObjectLoader;
import tech.threekilogram.depository.stream.StreamLoader;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateValue;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class BeautyModel {

      private static final String TAG = BeautyModel.class.getSimpleName();

      private static WeakReference<MainActivity> sRef;

      private static LocalCategoryBean sLocalBean = new LocalCategoryBean();

      /**
       * 初始化
       */
      public static void init ( ) {

            if( sLocalBean.getUrls() == null ) {
                  buildLocalBean();
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  /* 1.读取本地 LocalCategoryBean 缓存*/
                  File localBean = FileManager.getLocalBeautyBeanFile();
                  if( localBean.exists() ) {

                        buildLocalBeanFromFile( localBean );
                  } else {

                        /* 2.没有beauty历史记录缓存 */
                        Log.e( TAG, "buildLocalBean : 从网络构建本地bean" );
                        if( NetWork.hasNetwork() ) {

                              /* 3.从网络下载,并构建bean*/
                              buildLocalBeanFromNet( localBean );
                        } else {

                              /* 3.如果无法从网络构建 */
                              sLocalBean.setUrls( new ArrayList<>() );
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
       * 从本地文件构建BeautiesBean
       */
      private static void buildLocalBeanFromFile ( File beanFile ) {

            Log.e( TAG, "buildLocalBean : 从本地缓存构建福利bean中" );
            /* 从缓存加载数据 */
            LocalCategoryBean bean = ObjectLoader.loadFromFile( beanFile, LocalCategoryBean.class );
            sLocalBean.setStartDate( bean.getStartDate() );
            sLocalBean.setUrls( bean.getUrls() );
            Log.e( TAG, "buildLocalBeanFromFile : 从本地缓存构建福利bean完成" );

            /* 唤醒等待beautiesBean创建的线程启动 */
            notifyAllWait();

            /* 从网络获取最新的数据 */
            if( NetWork.hasNetwork() ) {

                  downLoadLatestBeautyJson();
            }
      }

      /**
       * 下载最新的福利数据,如果没有缓存过那么添加到BeautiesBean缓存
       */
      private static void downLoadLatestBeautyJson ( ) {

            Date date = DateUtil.getDate( sLocalBean.getStartDate() );
            File jsonFile = BeanLoader.downLoadLatestBeautyJson( date );

            JsonUtil.parserLatestBeautyJson( jsonFile, date, sLocalBean );

            boolean delete = jsonFile.delete();
      }

      /**
       * 从网络构建BeautiesBean
       */
      private static void buildLocalBeanFromNet ( File beautiesBeanFile ) {

            File beautyJsonFile = FileManager.getBeautyJsonFile();
            if( beautyJsonFile.exists() ) {
                  boolean delete = beautyJsonFile.delete();
            }
            String url = GankUrl.beautyAllUrl();
            Log.e(
                TAG,
                "buildLocalBean : 从网络下载所有福利数据.json中: " + url
            );
            StreamLoader.downLoad( url, beautyJsonFile );
            Log.e(
                TAG,
                "buildLocalBean : 从网络下载所有福利数据.json完成: " + beautyJsonFile
            );

            List<String> result = new ArrayList<>();
            sLocalBean.setUrls( result );
            JsonUtil.parseDownLoadAllBeautyJson( beautyJsonFile, sLocalBean );

            /* 唤醒等待beautiesBean创建的线程启动 */
            notifyAllWait();

            /* 缓存最新数据 */
            ObjectLoader.toFile( beautiesBeanFile, sLocalBean, LocalCategoryBean.class );
            Log.e( TAG, "buildLocalBean : 缓存网络构建beautyBean到文件完成: " + beautiesBeanFile.exists()
                + " " + beautiesBeanFile );
      }

      /**
       * 获取构建好的bean
       */
      public static LocalCategoryBean getLocalBean ( ) {

            if( sLocalBean.getUrls() == null ) {
                  synchronized(GankUrl.BEAUTY) {
                        if( sLocalBean.getUrls() == null ) {
                              try {
                                    GankUrl.BEAUTY.wait();
                              } catch(InterruptedException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            }
            return sLocalBean;
      }

      /**
       * 获取构建好的bean中的image Urls
       */
      public static List<String> getUrls ( ) {

            if( sLocalBean.getUrls() == null ) {
                  synchronized(GankUrl.BEAUTY) {
                        if( sLocalBean.getUrls() == null ) {
                              try {
                                    GankUrl.BEAUTY.wait();
                              } catch(InterruptedException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            }
            return sLocalBean.getUrls();
      }

      /**
       * 绑定宿主
       */
      public static void bind ( MainActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      /**
       * 为{@link MainActivity}获取banner图片
       */
      public static void loadBannerBitmap ( ) {

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
