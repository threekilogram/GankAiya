package com.example.wuxio.gankexamples.main;

import android.widget.Toast;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.model.BeanLoader;
import com.example.wuxio.gankexamples.model.bean.BeautiesBean;
import com.example.wuxio.gankexamples.utils.NetWork;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Liujin 2018-09-12:14:54
 */
public class BeautyModel {

      private static final String TAG = BeautyModel.class.getSimpleName();

      private static WeakReference<MainActivity> sRef;

      private static final BeautiesBean  sBeautiesBean = new BeautiesBean();
      public static final  AtomicBoolean sIsBeanBuild  = new AtomicBoolean();

      /**
       * 正在构建bean时的锁
       */
      public static final Object LOCK_BUILDING_BEAUTIES_BEAN = new Object();

      public static void init ( ) {

            if( sBeautiesBean.getBeautyUrls() == null ) {
                  sIsBeanBuild.set( false );
                  BeanLoader.buildBeautiesBean( sBeautiesBean );
            } else {
                  BeanLoader.loadLatestBeautyJson( sBeautiesBean );
            }
      }

      public static BeautiesBean getBeautiesBean ( ) {

            return sBeautiesBean;
      }

      public static void bind ( MainActivity activity ) {

            sRef = new WeakReference<>( activity );
      }

      public static void loadBannerBitmap ( ) {

            BeanLoader.loadListBitmaps(
                sBeautiesBean,
                0,
                5,
                ( index, count, result ) -> {

                      if( result == null ) {
                            if( NetWork.hasNetwork() ) {
                                  Toast.makeText(
                                      App.INSTANCE,
                                      "无法获取banner图片",
                                      Toast.LENGTH_SHORT
                                  ).show();
                            }
                      } else {

                            MainActivity mainActivity = sRef.get();
                            if( mainActivity != null ) {
                                  mainActivity.setBannerBitmaps( result );
                            }
                      }
                }
            );
      }
}
