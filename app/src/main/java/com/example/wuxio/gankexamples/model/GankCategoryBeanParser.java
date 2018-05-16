package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-04:21:25
 */
public class GankCategoryBeanParser {

    public static GankCategoryBean parseCategory(JsonReader jsonReader) {

        GankCategoryBean bean = new GankCategoryBean();

        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {

                String name = jsonReader.nextName();
                if ("_id".equals(name)) {

                    bean._id = (jsonReader.nextString());

                } else if ("createdAt".equals(name)) {

                    bean.createdAt = jsonReader.nextString();

                } else if ("desc".equals(name)) {

                    bean.desc = jsonReader.nextString();

                } else if ("publishedAt".equals(name)) {

                    bean.publishedAt = jsonReader.nextString();

                } else if ("source".equals(name)) {

                    bean.source = jsonReader.nextString();

                } else if ("type".equals(name)) {

                    bean.type = jsonReader.nextString();

                } else if ("url".equals(name)) {

                    bean.url = jsonReader.nextString();

                } else if ("used".equals(name)) {

                    bean.used = jsonReader.nextBoolean();

                } else if ("who".equals(name) && jsonReader.peek() != JsonToken.NULL) {

                    bean.who = jsonReader.nextString();

                } else if ("images".equals(name)) {

                    bean.images = parseImages(jsonReader);
                } else {

                    jsonReader.skipValue();
                }
            }

            jsonReader.endObject();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return bean;
    }


    private static List< String > parseImages(JsonReader jsonReader) {

        List< String > list = new ArrayList<>();

        try {
            jsonReader.beginArray();

            while (jsonReader.hasNext()) {

                String string = jsonReader.nextString();
                list.add(string);
            }

            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}