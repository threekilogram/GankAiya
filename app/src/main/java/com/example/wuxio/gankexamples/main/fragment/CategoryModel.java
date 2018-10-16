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
import java.util.List;
import tech.threekilogram.depository.cache.json.JsonLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      /**
       * 内存中缓存数据数量
       */
      private static final int COUNT = 100;

      private static CategoryModel sAndroid = new CategoryModel( GankUrl.ANDROID );

      private final String TAG;
      private final String CATEGORY;

      /**
       * 保存加载{@link GankCategoryItem}
       */
      private JsonLoader<GankCategoryItem> sItemLoader;
      /**
       * 本地缓存索引
       */
      private LocalCategoryBean            sLocalBean;

      /**
       * 分类数据缓存文件夹
       */
      private File mCategoryFile;
      /**
       * 本地索引缓存
       */
      private File mLocalBeanFile;
      /**
       * 最新的数据缓存
       */
      private File mLatestJsonFile;
      /**
       * 历史数据缓存
       */
      private File mJsonFile;

      public static CategoryModel instance ( String type ) {

            if( GankUrl.ANDROID.equals( type ) ) {
                  return sAndroid;
            }

            return sAndroid;
      }

      /**
       * 工具方法,初始化所有分类
       */
      public static void init ( ) {

            sAndroid.initField();
      }

      private CategoryModel ( String category ) {

            TAG = CategoryModel.class.getSimpleName() + " : " + category;
            CATEGORY = category;

            mCategoryFile = new File( FileManager.getAppFile(), category );
            mLocalBeanFile = new File( mCategoryFile, category + "_all" );
            mLatestJsonFile = new File( mCategoryFile, category + "_latest.json" );
            mJsonFile = new File( mCategoryFile, category + ".json" );

            if( !mCategoryFile.exists() ) {
                  boolean mkdirs = mCategoryFile.mkdirs();
            }
      }

      public void initField ( ) {

            if( sItemLoader == null ) {

                  sItemLoader = new JsonLoader<>(
                      COUNT,
                      mCategoryFile,
                      GankCategoryItem.class
                  );
            }

            if( sLocalBean == null ) {
                  sLocalBean = new LocalCategoryBean();
                  Log.e( TAG, "init : 初次启动 初始化索引 " + CATEGORY );
                  buildLocalBean();
            } else {
                  Log.e( TAG, "init : 再次启动 更新索引 " + CATEGORY );
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  if( mLocalBeanFile.exists() ) {

                        Log.e(
                            TAG, "buildLocalBean : 从本地文件构建索引中 " + CATEGORY + " " + mLocalBeanFile );

                        sLocalBean = Model.buildLocalBeanFromFile(
                            CATEGORY,
                            mLocalBeanFile,
                            mLatestJsonFile
                        );
                        Log.e(
                            TAG,
                            "buildLocalBean : 从本地文件构建索引完成 " + CATEGORY + " " + sLocalBean
                                .getUrls().size()
                        );

                        notifyAllWait();

                        Log.e( TAG, "buildLocalBean : 从最新的json保存数据到本地索引中 " + CATEGORY + " " );
                        JsonUtil.parserJsonToItemJson( mLatestJsonFile, sItemLoader );
                        Log.e(
                            TAG,
                            "buildLocalBean : 从最新的json中保存数据数据到本地索引完成 " + CATEGORY + " "
                        );
                  } else {

                        if( NetWork.hasNetwork() ) {

                              Log.e(
                                  TAG,
                                  "buildLocalBean : 从网络构建索引中 " + CATEGORY + " "
                              );

                              sLocalBean = Model.buildLocalBeanFromNet(
                                  GankUrl.category( CATEGORY, Integer.MAX_VALUE, 1 ),
                                  mJsonFile,
                                  mLocalBeanFile
                              );
                              Log.e(
                                  TAG,
                                  "buildLocalBean : 从网络构建索引完成 " + CATEGORY + " "
                                      + sLocalBean.getUrls().size()
                              );

                              notifyAllWait();

                              Log.e(
                                  TAG,
                                  "buildLocalBean : 从网络json中分离item数据中 " + CATEGORY + " "
                              );
                              JsonUtil.parserJsonToItemJson( mJsonFile, sItemLoader );
                              Log.e(
                                  TAG, "buildLocalBean : 从网络json中分离item数据完成 " + CATEGORY
                                      + " " );
                        } else {

                              sLocalBean = new LocalCategoryBean();
                              sLocalBean.setUrls( new ArrayList<>() );
                              Log.e(
                                  TAG, "buildLocalBean : 没有网络 无法从网络构建索引 " + CATEGORY
                                      + " " );
                              notifyAllWait();
                        }
                  }
            } );
      }

      private void notifyAllWait ( ) {

            synchronized(CATEGORY) {
                  CATEGORY.notifyAll();
            }
      }

      private void waitLocalBuild ( ) {

            if( sLocalBean.getUrls() == null ) {
                  synchronized(CATEGORY) {
                        if( sLocalBean.getUrls() == null ) {
                              try {
                                    CATEGORY.wait();
                              } catch(InterruptedException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            }
      }

      public LocalCategoryBean getLocalBean ( ) {

            waitLocalBuild();
            return sLocalBean;
      }

      public List<String> getLocalBeanUrls ( ) {

            waitLocalBuild();
            return sLocalBean.getUrls();
      }

      public GankCategoryItem getItemFromMemory ( int position ) {

            List<String> urls = getLocalBeanUrls();
            return sItemLoader.loadFromMemory( urls.get( position ) );
      }

      public GankCategoryItem getItem ( int position ) {

            List<String> urls = getLocalBeanUrls();
            String url = urls.get( position );
            return sItemLoader.load( url );
      }
}
