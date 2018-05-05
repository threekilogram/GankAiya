package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;
import android.util.JsonToken;

import com.example.wuxio.gankexamples.utils.date.DateFormat;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-02:17:15
 */
public class CategoryResultParser {

    public static CategoryResult parse(Reader reader) {

        CategoryResult entity = new CategoryResult();
        JsonReader jsonReader = new JsonReader(reader);

        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {

                String name = jsonReader.nextName();

                if ("error".equals(name)) {

                    entity.error = jsonReader.nextBoolean();

                } else if ("results".equals(name)) {

                    entity.results = (parseArray(jsonReader));
                }
            }

            jsonReader.endObject();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {

                reader.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return entity;
    }


    private static List< ResultsBean > parseArray(JsonReader jsonReader) {

        List< ResultsBean > results = new ArrayList<>();

        try {

            jsonReader.beginArray();

            while (jsonReader.hasNext()) {

                ResultsBean bean = parseEntity(jsonReader);
                results.add(bean);
            }

            jsonReader.endArray();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return results;
    }


    private static ResultsBean parseEntity(JsonReader jsonReader) {

        ResultsBean bean = new ResultsBean();

        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {

                String name = jsonReader.nextName();
                if (name.equals("_id")) {

                    bean._id = (jsonReader.nextString());

                } else if (name.equals("createdAt")) {

                    String createdAt = jsonReader.nextString();
                    bean.createdAt = DateFormat.format(createdAt);

                } else if (name.equals("desc")) {

                    bean.desc = (jsonReader.nextString());

                } else if (name.equals("publishedAt")) {

                    String publishedAt = jsonReader.nextString();
                    bean.publishedAt = DateFormat.format(publishedAt);

                } else if (name.equals("source")) {

                    bean.source = (jsonReader.nextString());

                } else if (name.equals("type")) {

                    bean.type = (jsonReader.nextString());

                } else if (name.equals("url")) {

                    bean.url = (jsonReader.nextString());

                } else if (name.equals("used")) {

                    bean.used = (jsonReader.nextBoolean());

                } else if (name.equals("who") && jsonReader.peek() != JsonToken.NULL) {

                    bean.who = (jsonReader.nextString());

                } else if (name.equals("images")) {

                    List< ImageBean > list = parseImages(jsonReader);
                    bean.images.addAll(list);

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


    private static List< ImageBean > parseImages(JsonReader jsonReader) {

        List< ImageBean > list = new ArrayList<>();

        try {
            jsonReader.beginArray();

            while (jsonReader.hasNext()) {

                String string = jsonReader.nextString();
                ImageBean url = new ImageBean();
                url.url = string;
                list.add(url);
            }

            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
