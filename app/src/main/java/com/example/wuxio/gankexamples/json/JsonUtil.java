package com.example.wuxio.gankexamples.json;

import android.util.JsonToken;
import android.util.Log;
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
import tech.threekilogram.model.cache.json.JsonLoader;
/**
 * @author Liujin 2018-10-14:9:27
 */
public class JsonUtil {

      private static final String TAG = JsonUtil.class.getSimpleName();

      public static LocalCategoryBean parseJsonToLocalBean ( File jsonFile ) {

            try {
                  LocalCategoryBean bean = new LocalCategoryBean();
                  bean.setUrls( new ArrayList<>() );

                  if( !jsonFile.exists() ) {
                        return bean;
                  }

                  FileReader reader = new FileReader( jsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
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
                              bean.getUrls().add( url );
                        }
                  }
                  jsonParser.finish();
                  return bean;
            } catch(IOException e) {
                  e.printStackTrace();
            }
            return null;
      }

      /**
       * 用于解析从网络下载的最新的福利数据,并且判断数据中所有日期是否早于缓存的日期,如果全部早于,那么需要获取更多最新的数据
       *
       * @param jsonFile 下载的最新的json文件
       * @param date 已经缓存到的json日期
       *
       * @return true : 所有日期都早于指定的日期
       */
      public static boolean parserJsonToGetIsNeedMore ( File jsonFile, Date date ) {

            boolean result = true;
            try {
                  if( !jsonFile.exists() ) {
                        return false;
                  }

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

            return result;
      }

      /**
       * 将最新的数据添加到LocalCategoryBean缓存
       */
      public static void parserLatestJson (
          File latestJsonFile, Date date, LocalCategoryBean beautiesBean ) {

            try {
                  if( !latestJsonFile.exists() ) {
                        return;
                  }

                  ArrayList<String> newData = new ArrayList<>();

                  FileReader reader = new FileReader( latestJsonFile );
                  JsonParser jsonParser = new JsonParser( reader );
                  jsonParser.start();

                  /* 先读取第一条数据 */
                  jsonParser.skipToString( "publishedAt" );
                  String publishedAt = jsonParser.readString( "publishedAt" );
                  if( publishedAt.equals( beautiesBean.getStartDate() ) ) {
                        jsonParser.finish();
                        return;
                  } else {
                        beautiesBean.setStartDate( publishedAt );
                        jsonParser.skipToString( "url" );
                        String url = jsonParser.readString( "url" );
                        if( url != null ) {
                              newData.add( url );
                        }
                  }

                  while( jsonParser.peek() != JsonToken.END_DOCUMENT ) {
                        jsonParser.skipToString( "publishedAt" );
                        publishedAt = jsonParser.readString( "publishedAt" );
                        if( publishedAt != null ) {
                              Date publishedDate = DateUtil.getDate( publishedAt );
                              if( DateUtil.isLater( date, publishedDate ) ) {
                                    jsonParser.skipToString( "url" );
                                    String url = jsonParser.readString( "url" );
                                    if( url != null ) {
                                          newData.add( url );
                                    }
                              } else {
                                    break;
                              }
                        }
                  }
                  jsonParser.finish();
                  beautiesBean.getUrls().addAll( 0, newData );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }

      public static void parserJsonToItemJson (
          File jsonFile, JsonLoader<GankCategoryItem> loader ) {

            try {

                  if( !jsonFile.exists() ) {
                        return;
                  }

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
                  Log.e( TAG, "parserJsonToItemJson : 解析数据完成 " + count + " " + jsonFile );
            } catch(IOException e) {
                  e.printStackTrace();
            }
      }
}
