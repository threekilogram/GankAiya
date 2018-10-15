package com.example.wuxio.gankexamples.main.fragment;

import android.util.Log;
import com.example.wuxio.gankexamples.model.BitmapCache;
import com.example.wuxio.gankexamples.model.GankUrl;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.threekilogram.objectbus.executor.MainExecutor;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Liujin 2018-10-15:11:30
 */
public class ShowModelManager {

      private static final String TAG = ShowModelManager.class.getSimpleName();

      public static WeakReference<ShowFragment> sAndroidRef;
      public static WeakReference<ShowFragment> sAppRef;
      public static WeakReference<ShowFragment> sIosRef;
      public static WeakReference<ShowFragment> sFrontRef;
      public static WeakReference<ShowFragment> sRecommendRef;
      public static WeakReference<ShowFragment> sExtraResourcesRef;
      public static WeakReference<ShowFragment> sRestVideoRef;

      public static void bind ( String category, ShowFragment showFragment ) {

            if( GankUrl.ANDROID.equals( category ) ) {
                  sAndroidRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.APP.equals( category ) ) {
                  sAppRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.IOS.equals( category ) ) {
                  sIosRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.FRONT.equals( category ) ) {
                  sFrontRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.RECOMMEND.equals( category ) ) {
                  sRecommendRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.EXTRA_RESOURCES.equals( category ) ) {
                  sExtraResourcesRef = new WeakReference<>( showFragment );
                  return;
            }

            if( GankUrl.REST_VIDEO.equals( category ) ) {
                  sRestVideoRef = new WeakReference<>( showFragment );
            }
      }

      public static void unBind ( String category, ShowFragment showFragment ) {

            if( GankUrl.ANDROID.equals( category ) ) {
                  sAndroidRef.clear();
                  sAndroidRef = null;
            }

            if( GankUrl.APP.equals( category ) ) {
                  sAppRef.clear();
                  sAppRef = null;
                  return;
            }

            if( GankUrl.IOS.equals( category ) ) {
                  sIosRef.clear();
                  sIosRef = null;
                  return;
            }

            if( GankUrl.FRONT.equals( category ) ) {
                  sFrontRef.clear();
                  sFrontRef = null;
                  return;
            }

            if( GankUrl.RECOMMEND.equals( category ) ) {
                  sRecommendRef.clear();
                  sRecommendRef = null;
                  return;
            }

            if( GankUrl.EXTRA_RESOURCES.equals( category ) ) {
                  sExtraResourcesRef.clear();
                  sExtraResourcesRef = null;
                  return;
            }

            if( GankUrl.REST_VIDEO.equals( category ) ) {
                  sRestVideoRef.clear();
                  sRestVideoRef = null;
            }
      }

      /**
       * 加载一个类型的数据
       */
      public static void loadItems ( String type ) {

            if( GankUrl.ANDROID.equals( type ) ) {
                  loadAndroidData();
            }
      }

      private static void loadAndroidData ( ) {

            PoolExecutor.execute( ( ) -> {

                  List<String> urls = AndroidModel.getAndroidLocalBeanUrls();
                  MainExecutor.execute( ( ) -> {
                        sAndroidRef.get().setAdapterData( urls );
                  } );
            } );
      }

      public static GankCategoryItem getItemFromMemory ( String type, int position ) {

            if( GankUrl.ANDROID.equals( type ) ) {
                  return AndroidModel.getItemFromMemory( position );
            }
            return null;
      }

      public static void loadItemFromFile ( String type, int position ) {

            PoolExecutor.execute( ( ) -> {

                  if( GankUrl.ANDROID.equals( type ) ) {
                        GankCategoryItem item = AndroidModel.getItemFromFile( position );
                        Log.e( TAG, "loadItemFromFile : " + position + " " + item );
                        if( item != null ) {

//                              List<String> images = item.getImages();
//                              if( images != null && images.size() > 0 ) {
//                                    String url = images.get( 0 );
//                                    BitmapCache.downLoadPicture( url );
//                              }

                              try {
                                    sAndroidRef.get().onItemChanged( position );
                              } catch(Exception e) {
                                    /* nothing worry about */
                              }
                        }
                  }
            } );
      }

      public static File getGifFile ( String url ) {

            return BitmapCache.getFile( url );
      }
}
