package com.example.wuxio.gankexamples.json;

import android.util.JsonToken;
import android.util.Log;
import com.example.wuxio.gankexamples.file.FileManager;
import com.example.wuxio.gankexamples.model.bean.GankCategoryItem;
import com.example.wuxio.gankexamples.model.bean.LocalCategoryBean;
import com.example.wuxio.gankexamples.utils.DateUtil;
import com.threekilogram.jsonparser.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tech.threekilogram.depository.cache.json.JsonLoader;
import tech.threekilogram.depository.cache.json.ObjectLoader;

/**
 * @author Liujin 2018-10-14:9:27
 */
public class JsonUtil {

      private static final String TAG = JsonUtil.class.getSimpleName();

      /**
       *
       * @param jsonFile
       * @param bean
       */
      public static void parseDownLoadAllBeautyJson ( File jsonFile, LocalCategoryBean bean ) {

            /* 只读取url */
            try {
                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
                  jsonParser.start();

                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "url" );
                        String url = jsonParser.readString( "url" );
                        if( url != null ) {
                              bean.getUrls().add( url );
                              Log.e(
                                  TAG, "parseDownLoadAllBeautyJson : 从网络构建BeautiesBean中: " + url );
                        }
                  }
                  jsonParser.finish();
                  Log.e(
                      TAG, "parseDownLoadAllBeautyJson : 从网络构建完成: " + bean.getUrls()
                                                                          .size() );
            } catch(IOException e) {
                  e.printStackTrace();
            }

            /* 只读取起始日期 */
            try {
                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
                  jsonParser.start();

                  jsonParser.skipToString( "publishedAt" );
                  String publishedAt = jsonParser.readString( "publishedAt" );
                  jsonParser.finish();

                  bean.setStartDate( publishedAt );
                  Log.e( TAG, "parseDownLoadAllBeautyJson : 解析网络福利json起始日期: " + publishedAt );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      /**
       * 用于解析从网络下载的最新的福利数据,并且判断数据中所有日期是否早于缓存的日期,如果全部早于,那么需要获取更多最新的数据
       *
       * @param jsonFile 下载的最新的json文件
       * @param date 已经缓存到的json日期
       *
       * @return true : 所有日期都早于指定的日期
       */
      public static boolean parserBeautyJsonToGetIsNeedMoreLatest ( File jsonFile, Date date ) {

            boolean result = true;
            try {
                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
                  jsonParser.start();
                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "publishedAt" );
                        String publishedAt = jsonParser.readString( "publishedAt" );
                        if( publishedAt != null ) {
                              Date jsonDate = DateUtil.getDate( publishedAt );
                              if( !DateUtil.isLater( date, jsonDate ) ) {
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
       * 将最新的福利数据添加到BeautiesBean缓存
       */
      public static void parserLatestBeautyJson (
          File jsonFile, Date date, LocalCategoryBean beautiesBean ) {

            try {
                  Log.e( TAG, "parserLatestBeautyJson :添加最新福利数据到BeautiesBean缓存 --> 解析最新的福利数据" );
                  ArrayList<String> newData = new ArrayList<>();

                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
                  jsonParser.start();

                  /* 先读取第一条数据 */
                  jsonParser.skipToString( "publishedAt" );
                  String publishedAt = jsonParser.readString( "publishedAt" );
                  if( publishedAt.equals( beautiesBean.getStartDate() ) ) {
                        Log.e(
                            TAG,
                            "parserLatestBeautyJson :添加最新福利数据到BeautiesBean缓存 --> 没有新福利数据需要添加 "
                                + publishedAt
                        );
                        jsonParser.finish();
                        return;
                  } else {
                        beautiesBean.setStartDate( publishedAt );
                        jsonParser.skipToString( "url" );
                        String url = jsonParser.readString( "url" );
                        if( url != null ) {
                              newData.add( url );
                        }
                        Log.e(
                            TAG,
                            "parserLatestBeautyJson :添加最新福利数据到BeautiesBean缓存--> 最新的福利数据日期:"
                                + publishedAt
                        );
                        Log.e(
                            TAG,
                            "parserLatestBeautyJson :添加最新福利数据到BeautiesBean缓存--> 解析到新福利数据: "
                                + url
                        );
                  }

                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "publishedAt" );
                        publishedAt = jsonParser.readString( "publishedAt" );
                        if( publishedAt != null ) {
                              Date date1 = DateUtil.getDate( publishedAt );
                              if( DateUtil.isLater( date, date1 ) ) {
                                    jsonParser.skipToString( "url" );
                                    String url = jsonParser.readString( "url" );
                                    if( url != null ) {
                                          newData.add( url );
                                    }
                                    Log.e(
                                        TAG,
                                        "parserLatestBeautyJson :添加最新福利数据到BeautiesBean缓存--> 解析到新福利数据: "
                                            + url
                                    );
                              } else {
                                    break;
                              }
                        }
                  }
                  jsonParser.finish();
                  beautiesBean.getUrls().addAll( 0, newData );

                  /* 缓存最新数据到本地 */
                  File beanFile = FileManager.getLocalBeautyBeanFile();
                  ObjectLoader.toFile( beanFile, beautiesBean, LocalCategoryBean.class );
                  Log.e( TAG, "parserLatestBeautyJson : 添加最新福利数据到BeautiesBean缓存--> 完成" );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      public static void parserCategoryJsonToLocalBean ( File jsonFile, LocalCategoryBean bean ) {

            try {
                  Log.e( TAG, "parserCategoryJsonToLocalBean : 解析分类数据 " + jsonFile );
                  JsonParser jsonParser = new JsonParser( new FileReader( jsonFile ) );

                  ArrayList<String> urls = new ArrayList<>();

                  jsonParser.start();
                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {

                        if( bean.getStartDate() == null ) {
                              jsonParser.skipToString( "publishedAt" );
                              String publishedAt = jsonParser.readString( "publishedAt" );
                              bean.setStartDate( publishedAt );
                        }

                        jsonParser.skipToString( "url" );
                        String url = jsonParser.readString( "url" );
                        if( url != null ) {
                              urls.add( url );
                        }
                  }
                  jsonParser.finish();

                  Log.e( TAG, "parserCategoryJsonToLocalBean : 解析数据数量: " + urls.size() );
                  bean.setUrls( urls );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      public static void parserBeanToFile ( File jsonFile, JsonLoader<GankCategoryItem> loader ) {

            try {
                  JsonParser jsonParser = new JsonParser( new FileReader( jsonFile ) );
                  jsonParser.start();
                  int count = 0;
                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {

                        jsonParser.skipToString( "_id" );
                        String id = jsonParser.readString( "_id" );
                        if( id != null ) {
                              GankCategoryItem item = new GankCategoryItem();
                              item.set_id( id );
                              String createdAt = jsonParser.readString( "createdAt" );
                              item.setCreatedAt( createdAt );
                              String desc = jsonParser.readString( "desc" );
                              item.setDesc( desc );
                              List<String> images = jsonParser.readStringArray( "images" );
                              item.setImages( images );
                              String publishedAt = jsonParser.readString( "publishedAt" );
                              item.setPublishedAt( publishedAt );
                              String source = jsonParser.readString( "source" );
                              item.setSource( source );
                              String type = jsonParser.readString( "type" );
                              item.setType( type );
                              String url = jsonParser.readString( "url" );
                              item.setUrl( url );
                              boolean used = jsonParser.readBoolean( "used" );
                              item.setUsed( used );
                              String who = jsonParser.readString( "who" );
                              item.setWho( who );
                              count++;
                              loader.saveToFile( url, item );
                        }
                  }
                  jsonParser.finish();

                  Log.e( TAG, "parserBeanToFile : 从网络解析保存所有分类bean完成 " + count );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }
}
