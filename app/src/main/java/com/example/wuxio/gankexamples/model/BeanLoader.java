package com.example.wuxio.gankexamples.model;

import android.graphics.Bitmap;
import android.util.JsonToken;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.main.BeautyModel;
import com.example.wuxio.gankexamples.model.bean.BeautiesBean;
import com.example.wuxio.gankexamples.model.bean.GankCategory;
import com.example.wuxio.gankexamples.splash.SplashModel;
import com.example.wuxio.gankexamples.utils.DateUtil;
import com.example.wuxio.gankexamples.utils.NetWork;
import com.threekilogram.jsonparser.JsonParser;
import com.threekilogram.objectbus.bus.ObjectBus;
import com.threekilogram.objectbus.executor.PoolExecutor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tech.threekilogram.depository.cache.bitmap.BitmapLoader;
import tech.threekilogram.depository.cache.json.ObjectLoader;
import tech.threekilogram.depository.stream.StreamLoader;
import tech.threekilogram.screen.ScreenSize;

/**
 * @author Liujin 2018-10-07:19:24
 */
public class BeanLoader {

      private static final String TAG = BeanLoader.class.getSimpleName();

      /**
       * 加载图片
       */
      private static BitmapLoader sBitmapLoader;

      /**
       * 初始化变量
       */
      public static void init ( ) {

            if( sBitmapLoader == null ) {
                  File pictureFile = FileManager.getPictureFile();
                  int memorySize = (int) Runtime.getRuntime().maxMemory() >> 3;
                  sBitmapLoader = new BitmapLoader(
                      memorySize,
                      pictureFile
                  );
            }
      }

      /**
       * 用于和数据层通信,加载完新的splash回调{@link #loadLatestBeautyUrl(OnLatestBeautyLoadedListener)}
       */
      public interface OnLatestBeautyLoadedListener {

            /**
             * 加载完成{@link SplashModel#setSplashImage()}
             *
             * @param url new mUrl
             */
            void onLoaded ( String url );
      }

      /**
       * 获取最新的beauty分类的最新的一条数据,{@link SplashModel#setSplashImage()}
       */
      public static void loadLatestBeautyUrl ( OnLatestBeautyLoadedListener listener ) {

            PoolExecutor.execute( ( ) -> {

                  /* 没有网络 */
                  if( !NetWork.hasNetwork() ) {
                        listener.onLoaded( null );
                        return;
                  }

                  /* 从网络加载最新的数据 */
                  String latestUrl = GankUrl.beautyLatestUrl();
                  GankCategory gankCategory = StreamLoader
                      .loadJsonFromNet( latestUrl, GankCategory.class );

                  Log.e( TAG, "loadLatestBeautyUrl : 获取到的最新beauty: " + gankCategory );
                  if( gankCategory != null ) {
                        String url = gankCategory.getResults().get( 0 ).getUrl();
                        listener.onLoaded( url );
                  } else {
                        listener.onLoaded( null );
                  }
            } );
      }

      /**
       * 下载url对应的图片,{@link SplashModel#setSplashImage()}
       */
      public static void downLoadPicture ( String url ) {

            boolean contains = sBitmapLoader.containsOfFile( url );
            Log.e( TAG, "downLoadPicture : 是否有图片缓存: " + url + " : " + contains );
            if( !contains ) {
                  PoolExecutor.execute( ( ) -> {

                        sBitmapLoader.download( url );
                        File file = sBitmapLoader.getFile( url );
                        Log.e( TAG, "downLoadPicture : 下载图片完成 :" + url + " " + file );
                  } );
            }
      }

      /**
       * 测试是否有url对应的图片的缓存,{@link SplashModel#setSplashImage()}
       *
       * @param url mUrl
       *
       * @return true : 由对应的缓存
       */
      public static boolean hasPictureCache ( String url ) {

            return sBitmapLoader.containsOfFile( url );
      }

      /**
       * 加载图片完成的回调,{@link #loadBitmap(String, int, int, OnBitmapLoadedListener)}
       */
      public interface OnBitmapLoadedListener {

            /**
             * 加载完成
             *
             * @param url mUrl
             * @param bitmap url对应图片,可能为null
             */
            void onLoaded ( String url, Bitmap bitmap );
      }

      /**
       * 加载图片,{@link SplashModel#setSplashImage()}
       */
      public static void loadBitmap (
          String url, int width, int height, OnBitmapLoadedListener listener ) {

            Bitmap bitmap = sBitmapLoader.loadFromMemory( url );
            if( bitmap != null ) {
                  listener.onLoaded( url, bitmap );
                  return;
            }

            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {

                  if( !sBitmapLoader.containsOfFile( url ) ) {
                        sBitmapLoader.download( url );
                  }

                  Bitmap result = sBitmapLoader.loadFromFile( url, width, height );
                  bus.setResult( url, result );
            } ).toMain( ( ) -> {

                  if( listener != null ) {
                        Bitmap bitmap1 = bus.getResultOff( url );
                        listener.onLoaded( url, bitmap1 );
                  }
            } ).run();
      }

      /**
       * 为{@link com.example.wuxio.gankexamples.main.BeautyModel#sBeautiesBean}初始化
       *
       * @param beautiesBean from {@link com.example.wuxio.gankexamples.main.BeautyModel#sBeautiesBean}
       */
      public static void buildBeautiesBean ( BeautiesBean beautiesBean ) {

            PoolExecutor.execute( ( ) -> {

                  /* 1.读取本地 BeautiesBean 缓存*/
                  File beanFile = FileManager.getBeautiesBeanFile();
                  if( beanFile.exists() ) {

                        /* 2.如果存在缓存,从缓存加载数据 */
                        BeautiesBean bean = ObjectLoader
                            .loadFromFile( beanFile, BeautiesBean.class );
                        beautiesBean.setStartDate( bean.getStartDate() );
                        beautiesBean.setBeautyUrls( bean.getBeautyUrls() );

                        /* 唤醒等待beautiesBean创建的线程启动 */
                        BeautyModel.sIsBeanBuild.set( true );
                        synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                              BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.notifyAll();
                        }
                        Log.e(
                            TAG,
                            "buildBeautiesBean : 有BeautiesBean: " +
                                beautiesBean.getBeautyUrls().size()
                        );

                        /* 3.从网络获取最新的数据,更新bean */
                        if( NetWork.hasNetwork() ) {
                              loadLatestBeautyJson( beautiesBean );
                        }
                  } else {

                        /* 2.没有beauty历史记录缓存,从网络下载,并构建bean */
                        Log.e( TAG, "buildBeautiesBean : 没有BeautiesBean,从网络构建" );
                        if( NetWork.hasNetwork() ) {
                              File beautyJsonFile = FileManager.getBeautyJsonFile();
                              StreamLoader.downLoad( GankUrl.beautyAllUrl(), beautyJsonFile );
                              Log.e(
                                  TAG,
                                  "buildBeautiesBean : 从网络下载所有福利数据完成: " + beautyJsonFile
                              );

                              List<String> result = new ArrayList<>();
                              beautiesBean.setBeautyUrls( result );
                              parseBeautyJson( beautyJsonFile, result, beautiesBean );

                              /* 唤醒等待beautiesBean创建的线程启动 */
                              BeautyModel.sIsBeanBuild.set( true );
                              synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                                    BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.notifyAll();
                              }
                              ObjectLoader.toFile( beanFile, beautiesBean, BeautiesBean.class );
                              Log.e( TAG, "buildBeautiesBean : 缓存福利json到beautyBean完成: " + beanFile
                                  .exists() );
                        } else {

                              /* 3.如果无法从网络构建 */
                              beautiesBean.setBeautyUrls( new ArrayList<>() );

                              /* 唤醒等待beautiesBean创建的线程启动 */
                              BeautyModel.sIsBeanBuild.set( true );
                              synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                                    BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.notifyAll();
                              }
                              Log.e( TAG, "buildBeautiesBean : 没有网络,无法获取历史福利数据" );
                        }
                  }
            } );
      }

      /**
       * 获取最新的福利数据
       */
      public static void loadLatestBeautyJson (
          BeautiesBean beautiesBean ) {

            int count = 20;
            final int page = 1;
            final String beautyUrl = GankUrl.beautyUrl( count, page );

            File jsonFile = FileManager.getLatestBeautyJsonFile();
            if( jsonFile.exists() ) {
                  jsonFile.delete();
            }

            PoolExecutor.execute( ( ) -> {

                  int count1 = 20;
                  final int page1 = 1;
                  /* 下载最新的数据 */
                  StreamLoader.downLoad( beautyUrl, jsonFile );
                  /* 如果最新的数据不够,那么增加数量,继续下载 */
                  Date date = DateUtil.getDate( beautiesBean.getStartDate() );
                  while( needMoreJson( jsonFile, date ) ) {
                        jsonFile.delete();
                        count1 += count1;
                        String beautyUrl1 = GankUrl.beautyUrl( count1, page1 );
                        StreamLoader.downLoad( beautyUrl1, jsonFile );
                  }
                  addLatestJsonToBeautiesBean( jsonFile, date, beautiesBean );
                  jsonFile.delete();
            } );
      }

      /**
       * 将最新的福利数据添加到BeautiesBean缓存
       */
      private static void addLatestJsonToBeautiesBean (
          File jsonFile, Date date, BeautiesBean beautiesBean ) {

            try {
                  ArrayList<String> newData = new ArrayList<>();

                  JsonParser jsonParser = new JsonParser();
                  FileReader reader = new FileReader( jsonFile );
                  jsonParser.start( reader );

                  jsonParser.skipToString( "publishedAt" );
                  String publishedAt = jsonParser.readString( "publishedAt" );
                  if( publishedAt.equals( beautiesBean.getStartDate() ) ) {
                        Log.e( TAG, "addLatestJsonToBeautiesBean : 没有新福利数据需要添加" );
                        jsonParser.finish();
                        return;
                  } else {
                        beautiesBean.setStartDate( publishedAt );
                        jsonParser.skipToString( "mUrl" );
                        String url = jsonParser.readString( "mUrl" );
                        if( url != null ) {
                              newData.add( url );
                        }
                        Log.e(
                            TAG,
                            "addLatestJsonToBeautiesBean : 新福利数据添加 " + publishedAt + " " + url
                        );
                  }

                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "publishedAt" );
                        publishedAt = jsonParser.readString( "publishedAt" );
                        if( publishedAt != null ) {
                              Date date1 = DateUtil.getDate( publishedAt );
                              if( DateUtil.isLater( date, date1 ) ) {
                                    jsonParser.skipToString( "mUrl" );
                                    String url = jsonParser.readString( "mUrl" );
                                    if( url != null ) {
                                          newData.add( url );
                                    }
                              } else {
                                    break;
                              }
                        }
                  }
                  jsonParser.finish();

                  beautiesBean.getBeautyUrls().addAll( 0, newData );
                  File beanFile = FileManager.getBeautiesBeanFile();
                  ObjectLoader.toFile( beanFile, beautiesBean, BeautiesBean.class );
                  Log.e( TAG, "addLatestJsonToBeautiesBean : 更新BeautiesBean缓存" );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      /**
       * 用于{@link #loadLatestBeautyJson(BeautiesBean)}判断是否已经加载到最新的数据
       */
      private static boolean needMoreJson ( File jsonFile, Date date ) {

            boolean result = true;
            try {
                  JsonParser jsonParser = new JsonParser();
                  FileReader reader = new FileReader( jsonFile );
                  jsonParser.start( reader );
                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "publishedAt" );
                        String publishedAt = jsonParser.readString( "publishedAt" );
                        if( publishedAt != null ) {
                              Date date1 = DateUtil.getDate( publishedAt );
                              if( !DateUtil.isLater( date, date1 ) ) {
                                    result = false;
                                    break;
                              }
                        }
                  }
                  jsonParser.finish();
            } catch(IOException e) {
                  e.printStackTrace();
            }

            Log.e( TAG, "needMoreJson : 是否需要获取更多数据-->福利: " + result );
            return result;
      }

      /**
       * 用于{@link #buildBeautiesBean(BeautiesBean)}从历史数据中初始化构建bean
       */
      private static void parseBeautyJson (
          File jsonFile, List<String> result, BeautiesBean bean ) {

            /* 只读取url */
            try {
                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser();
                  jsonParser.start( reader );

                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "mUrl" );
                        String url = jsonParser.readString( "mUrl" );
                        if( url != null ) {
                              result.add( url );
                              Log.e( TAG, "parseBeautyJson : 解析福利json: " + url );
                        }
                  }
                  jsonParser.finish();
                  Log.e( TAG, "parseBeautyJson : 一共解析福利json: " + result.size() );
            } catch(IOException e) {
                  e.printStackTrace();
            }

            /* 只读取起始日期 */
            try {
                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser();
                  jsonParser.start( reader );

                  jsonParser.skipToString( "publishedAt" );
                  String publishedAt = jsonParser.readString( "publishedAt" );
                  jsonParser.finish();

                  bean.setStartDate( publishedAt );
                  Log.e( TAG, "parseBeautyJson : 福利起始日期: " + publishedAt );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      /**
       * {@link #loadListBitmaps(BeautiesBean, int, int, OnListBitmapsLoadedListener)}回调
       */
      public interface OnListBitmapsLoadedListener {

            /**
             * 加载完成
             *
             * @param index 起始
             * @param count 数量
             * @param result bitmaps
             */
            void onLoaded ( int index, int count, List<Bitmap> result );
      }

      public static void loadListBitmaps (
          BeautiesBean beautiesBean, int index, int count, OnListBitmapsLoadedListener listener ) {

            String key = GankUrl.BEAUTY + "_" + index + "_" + count;

            ObjectBus bus = ObjectBus.newList();
            bus.toPool( ( ) -> {

                  List<String> beautiesUrl = getBeautiesUrl( beautiesBean );
                  int size = beautiesUrl.size();
                  if( size == 0 ) {
                        /* 构建完成后还是没有数据 */
                        return;
                  }
                  if( index + count < size ) {

                        /* 1.找出没有缓存的图片 */
                        ArrayList<String> notCacheUrl = new ArrayList<>();
                        for( int i = index; i < index + count; i++ ) {
                              String url = beautiesUrl.get( i );
                              if( !sBitmapLoader.containsOfFile( url ) ) {
                                    notCacheUrl.add( url );
                              }
                              Log.e( TAG, "loadListBitmaps : 加载一组图片: " + url );
                        }

                        /* 2.缓存没有缓存的图片 */
                        int size1 = notCacheUrl.size();
                        if( size1 > 0 ) {
                              ArrayList<DownLoadBitmapRunnable> runnableList = new ArrayList<>(
                                  size1 );
                              for( String s : notCacheUrl ) {
                                    Log.e( TAG, "loadListBitmaps : 加载一组图片:没有缓存的图片 " + s );
                                    runnableList.add( new DownLoadBitmapRunnable( s ) );
                              }

                              PoolExecutor.execute( runnableList );
                              Log.e( TAG, "loadListBitmaps : 加载一组图片:缓存没有缓存的图片完成" );
                        }

                        /* 3.加载成bitmap */
                        ArrayList<Bitmap> result = new ArrayList<>( count );
                        int width = ScreenSize.getWidth();
                        int height = ScreenSize.getHeight();
                        for( int i = index; i < index + count; i++ ) {
                              String url = beautiesUrl.get( i );
                              Bitmap bitmap = sBitmapLoader.load( url, width, height );
                              result.add( bitmap );
                        }

                        Log.e( TAG, "loadListBitmaps : 加载一组图片完成" );
                        bus.setResult( key, result );
                  }
            } ).toMain( ( ) -> {

                  List<Bitmap> result = bus.getResultOff( key );
                  listener.onLoaded( index, count, result );
            } ).run();
      }

      private static List<String> getBeautiesUrl ( BeautiesBean beautiesBean ) {

            if( !BeautyModel.sIsBeanBuild.get() ) {
                  synchronized(BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN) {
                        try {
                              BeautyModel.LOCK_BUILDING_BEAUTIES_BEAN.wait();
                        } catch(InterruptedException e) {
                              e.printStackTrace();
                        }
                  }
            }
            return beautiesBean.getBeautyUrls();
      }

      /**
       * 下载图片的runnable
       */
      private static class DownLoadBitmapRunnable implements Runnable {

            private String mUrl;

            public DownLoadBitmapRunnable ( String url ) {

                  this.mUrl = url;
            }

            @Override
            public void run ( ) {

                  sBitmapLoader.download( mUrl );
            }
      }
}
