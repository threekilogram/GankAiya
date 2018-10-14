package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.BeautiesBean;
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
import java.util.concurrent.atomic.AtomicBoolean;
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

      private static       BeautiesBean  sBeautiesBean = new BeautiesBean();
      private static final AtomicBoolean IS_BEAN_BUILD = new AtomicBoolean();

      /**
       * 正在构建bean时的锁
       */
      private static final Object LOCK_BUILDING_BEAUTIES_BEAN = new Object();

      /**
       * 初始化
       */
      public static void init ( ) {

            if( sBeautiesBean.getBeautyUrls() == null ) {
                  IS_BEAN_BUILD.set( false );
                  buildBeautiesBean();
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private static void buildBeautiesBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  /* 1.读取本地 BeautiesBean 缓存*/
                  File beautiesBeanFile = FileManager.getBeautiesBeanFile();
                  if( beautiesBeanFile.exists() ) {

                        buildBeautiesBeanFromFile( beautiesBeanFile );
                  } else {

                        /* 2.没有beauty历史记录缓存 */
                        Log.e( TAG, "buildBeautiesBean : 从网络构建福利bean" );
                        if( NetWork.hasNetwork() ) {

                              /* 3.从网络下载,并构建bean*/
                              buildBeautiesBeanFromNet( beautiesBeanFile );
                        } else {

                              /* 3.如果无法从网络构建 */
                              sBeautiesBean.setBeautyUrls( new ArrayList<>() );
                              /* 唤醒等待beautiesBean创建的线程启动 */
                              notifyAllWait();
                              Log.e( TAG, "buildBeautiesBean : 没有网络,无法获取历史福利数据" );
                        }
                  }

                  cacheBeautyPicture();
            } );
      }

      private static void notifyAllWait ( ) {

            BeautyModel.IS_BEAN_BUILD.set( true );
            synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                  BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.notifyAll();
            }
      }

      /**
       * 缓存福利图片文件
       */
      private static void cacheBeautyPicture ( ) {

            List<String> beautyUrls = getBeautiesUrl();

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
      private static void buildBeautiesBeanFromFile ( File beanFile ) {

            Log.e( TAG, "buildBeautiesBean : 从本地缓存构建福利bean中" );
            /* 从缓存加载数据 */
            BeautiesBean bean = ObjectLoader.loadFromFile( beanFile, BeautiesBean.class );
            sBeautiesBean.setStartDate( bean.getStartDate() );
            sBeautiesBean.setBeautyUrls( bean.getBeautyUrls() );
            Log.e( TAG, "buildBeautiesBeanFromFile : 从本地缓存构建福利bean完成" );

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

            Date date = DateUtil.getDate( sBeautiesBean.getStartDate() );
            File jsonFile = BeanLoader.downLoadLatestBeautyJson( date );

            JsonUtil.parserLatestBeautyJson( jsonFile, date, sBeautiesBean );

            boolean delete = jsonFile.delete();
      }

      /**
       * 从网络构建BeautiesBean
       */
      private static void buildBeautiesBeanFromNet ( File beautiesBeanFile ) {

            File beautyJsonFile = FileManager.getBeautyJsonFile();
            if( beautyJsonFile.exists() ) {
                  boolean delete = beautyJsonFile.delete();
            }
            String url = GankUrl.beautyAllUrl();
            Log.e(
                TAG,
                "buildBeautiesBean : 从网络下载所有福利数据.json中: " + url
            );
            StreamLoader.downLoad( url, beautyJsonFile );
            Log.e(
                TAG,
                "buildBeautiesBean : 从网络下载所有福利数据.json完成: " + beautyJsonFile
            );

            List<String> result = new ArrayList<>();
            sBeautiesBean.setBeautyUrls( result );
            JsonUtil.parseDownLoadAllBeautyJson( beautyJsonFile, sBeautiesBean );

            /* 唤醒等待beautiesBean创建的线程启动 */
            notifyAllWait();

            /* 缓存最新数据 */
            ObjectLoader.toFile( beautiesBeanFile, sBeautiesBean, BeautiesBean.class );
            Log.e( TAG, "buildBeautiesBean : 缓存网络构建beautyBean到文件完成: " + beautiesBeanFile.exists()
                + " " + beautiesBeanFile );
      }

      /**
       * 获取构建好的bean
       */
      public static BeautiesBean getBeautiesBean ( ) {

            if( !IS_BEAN_BUILD.get() ) {
                  synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                        try {
                              BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.wait();
                        } catch(InterruptedException e) {
                              e.printStackTrace();
                        }
                  }
            }
            return sBeautiesBean;
      }

      /**
       * 获取构建好的bean中的image Urls
       */
      public static List<String> getBeautiesUrl ( ) {

            if( !IS_BEAN_BUILD.get() ) {
                  synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                        try {
                              BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.wait();
                        } catch(InterruptedException e) {
                              e.printStackTrace();
                        }
                  }
            }
            return sBeautiesBean.getBeautyUrls();
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

                  List<String> beautiesUrl = getBeautiesUrl();

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
