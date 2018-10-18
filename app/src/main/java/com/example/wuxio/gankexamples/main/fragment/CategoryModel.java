package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.json.JsonUtil;
import com.example.wuxio.gankexamples.log.AppLog;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.Model;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.executor.PoolExecutor;
import tech.threekilogram.model.cache.json.JsonLoader;
import tech.threekilogram.model.cache.json.ObjectLoader;

/**
 * @author Liujin 2018-10-14:20:57
 */
public class CategoryModel {

      private static CategoryModel sAndroid       = new CategoryModel( GankUrl.ANDROID );
      private static CategoryModel sApp           = new CategoryModel( GankUrl.APP );
      private static CategoryModel sIos           = new CategoryModel( GankUrl.IOS );
      private static CategoryModel sFront         = new CategoryModel( GankUrl.FRONT );
      private static CategoryModel sRecommend     = new CategoryModel( GankUrl.RECOMMEND );
      private static CategoryModel sExtraResource = new CategoryModel( GankUrl.EXTRA_RESOURCES );
      private static CategoryModel sRestVideo     = new CategoryModel( GankUrl.REST_VIDEO );

      private final String TAG;
      private final String CATEGORY;

      /**
       * 保存加载{@link GankCategoryItem}
       */
      private JsonLoader<GankCategoryItem> mJsonLoader;
      /**
       * 本地缓存索引
       */
      private LocalCategoryBean            mLocalCategoryBean;
      /**
       * 分类数据缓存文件夹
       */
      private File                         mCategoryFile;
      /**
       * 本地索引缓存
       */
      private File                         mLocalBeanFile;
      /**
       * 最新的数据缓存
       */
      private File                         mLatestJsonFile;
      /**
       * 历史数据缓存
       */
      private File                         mJsonFile;

      public static CategoryModel instance ( String type ) {

            if( GankUrl.ANDROID.equals( type ) ) {
                  return sAndroid;
            }
            if( GankUrl.APP.equals( type ) ) {
                  return sApp;
            }
            if( GankUrl.IOS.equals( type ) ) {
                  return sIos;
            }
            if( GankUrl.FRONT.equals( type ) ) {
                  return sFront;
            }
            if( GankUrl.RECOMMEND.equals( type ) ) {
                  return sRecommend;
            }
            if( GankUrl.EXTRA_RESOURCES.equals( type ) ) {
                  return sExtraResource;
            }
            if( GankUrl.REST_VIDEO.equals( type ) ) {
                  return sRestVideo;
            }
            return sAndroid;
      }

      /**
       * 工具方法,初始化所有分类
       */
      public static void init ( ) {

            sAndroid.initField();
            sApp.initField();
            sIos.initField();
            sFront.initField();
            sRecommend.initField();
            sExtraResource.initField();
            sRestVideo.initField();
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

            if( mJsonLoader == null ) {

                  mJsonLoader = new JsonLoader<>(
                      -1,
                      mCategoryFile,
                      GankCategoryItem.class
                  );
            }

            if( mLocalCategoryBean == null ) {
                  mLocalCategoryBean = new LocalCategoryBean();
                  buildLocalBean();
            } else {
            }
      }

      /**
       * 从文件或者从网络构建bean
       */
      private void buildLocalBean ( ) {

            PoolExecutor.execute( ( ) -> {

                  if( mLocalBeanFile.exists() ) {

                        AppLog.addLog( "从本地文件构建索引中 " + CATEGORY + " " + mLocalBeanFile );

                        mLocalCategoryBean = ObjectLoader
                            .loadFromFile( mLocalBeanFile, LocalCategoryBean.class );

                        AppLog.addLog( "从本地文件构建索引完成 " + CATEGORY + " " + mLocalCategoryBean
                            .getUrls().size() );

                        /* 从网络获取最新的数据 */
                        int newJson = 0;
                        if( NetWork.hasNetwork() ) {
                              newJson = Model.downLoadLatestJson(
                                  CATEGORY,
                                  mLocalCategoryBean,
                                  mLatestJsonFile,
                                  mLocalBeanFile
                              );
                              AppLog.addLog( "从网络获取最新数据 " + CATEGORY + " 数量 " + newJson );
                        }

                        notifyAllWait();

                        if( newJson > 0 ) {
                              AppLog.addLog( "将最新的json数据分解成item数据开始 " + CATEGORY );
                              JsonUtil.parserJsonToItemJson( mLatestJsonFile, mJsonLoader );
                              AppLog.addLog( "将最新的json数据分解成item数据完成 " + CATEGORY );
                        }
                  } else {

                        if( NetWork.hasNetwork() ) {

                              AppLog.addLog( "从网络构建索引中 " + CATEGORY );

                              mLocalCategoryBean = Model.buildLocalBeanFromNet(
                                  GankUrl.category( CATEGORY, Integer.MAX_VALUE, 1 ),
                                  mJsonFile,
                                  mLocalBeanFile
                              );
                              AppLog.addLog(
                                  "从网络构建索引完成 "
                                      + CATEGORY + " "
                                      + mLocalCategoryBean.getUrls().size()
                              );

                              notifyAllWait();

                              AppLog.addLog( "将最新的json数据分解成item数据开始 " + CATEGORY );
                              JsonUtil.parserJsonToItemJson( mLatestJsonFile, mJsonLoader );
                              AppLog.addLog( "将最新的json数据分解成item数据完成 " + CATEGORY );
                        } else {

                              mLocalCategoryBean = new LocalCategoryBean();
                              mLocalCategoryBean.setUrls( new ArrayList<>() );
                              notifyAllWait();

                              AppLog
                                  .addLog( "构建索引 " + " " + CATEGORY + " 失败 " + " 没有本地缓存,没有网络可以加载" );
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

            if( mLocalCategoryBean.getUrls() == null ) {
                  synchronized(CATEGORY) {
                        if( mLocalCategoryBean.getUrls() == null ) {
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
            return mLocalCategoryBean;
      }

      public List<String> getLocalBeanUrls ( ) {

            waitLocalBuild();
            return mLocalCategoryBean.getUrls();
      }

      public GankCategoryItem getItemFromMemory ( int position ) {

            List<String> urls = getLocalBeanUrls();
            return mJsonLoader.loadFromMemory( urls.get( position ) );
      }

      public GankCategoryItem getItem ( int position ) {

            List<String> urls = getLocalBeanUrls();
            String url = urls.get( position );
            return mJsonLoader.load( url );
      }
}
