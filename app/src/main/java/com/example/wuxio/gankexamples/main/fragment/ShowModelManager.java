package com.example.wuxio.gankexamples.main.fragment;

import com.example.wuxio.gankexamples.model.GankUrl;
import java.lang.ref.WeakReference;

/**
 * @author Liujin 2018-10-15:11:30
 */
public class ShowModelManager {

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
            }
      }

      public static void unBind ( String category, ShowFragment showFragment ) {

            if( GankUrl.ANDROID.equals( category ) ) {
                  sAndroidRef.clear();
                  sAndroidRef = null;
            }
      }
}
