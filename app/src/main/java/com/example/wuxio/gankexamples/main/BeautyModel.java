package com.example.wuxio.gankexamples.main;

import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.bean.BeautiesBean;
import java.lang.ref.WeakReference;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class BeautyModel {

      private static final String TAG = BeautyModel.class.getSimpleName();

      private static WeakReference<MainActivity> sRef;

      private static final BeautiesBean sBeautiesBean = new BeautiesBean();

      /**
       * 正在构建bean时的锁
       */
      public static final Object LOCK_BUILDING_BEAUTIES_BEAN = new Object();

      public static void init ( ) {

            if( sBeautiesBean.getBeautyUrls() == null ) {
                  BeanLoader.buildBeautiesBean( sBeautiesBean );
            } else {
                  BeanLoader.loadLatestBeautyJson( sBeautiesBean );
            }
      }

      public static void bind ( MainActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      public static void loadBannerBitmap ( ) {

      }
}
