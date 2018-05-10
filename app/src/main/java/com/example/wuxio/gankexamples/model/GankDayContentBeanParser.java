package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;

import com.example.wuxio.gankexamples.constant.GankCategory;

import java.io.IOException;
import java.io.Reader;

/**
 * @author wuxio 2018-05-10:8:12
 */
public class GankDayContentBeanParser {

    public static GankDayContentBean parse(Reader reader) {

        JsonReader jsonReader = new JsonReader(reader);

        try {

            GankDayContentBean dayContentBean = new GankDayContentBean();
            final String firstName = "category";
            final String secondName = "error";
            final String thirdName = "results";
            while (jsonReader.hasNext()) {

                String nextName = jsonReader.nextName();

                if (firstName.equals(nextName)) {

                    parseContentType(jsonReader, dayContentBean);

                } else if (secondName.equals(nextName)) {

                    boolean nextBoolean = jsonReader.nextBoolean();

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                jsonReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public static void parseContentType(
            JsonReader jsonReader,
            GankDayContentBean dayContentBean) throws IOException {

        String nextName;
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {

            nextName = jsonReader.nextName();

            if (GankCategory.Android.equals(nextName)) {
                dayContentBean.setHasAndroid();
            } else if (GankCategory.App.equals(nextName)) {
                dayContentBean.setHasApp();
            } else if (GankCategory.BEAUTY.equals(nextName)) {
                dayContentBean.setHasBeauty();
            } else if (GankCategory.EXTRA_RESOURCES.equals(nextName)) {
                dayContentBean.setHasExtraResources();
            } else if (GankCategory.FRONT.equals(nextName)) {
                dayContentBean.setHasFront();
            } else if (GankCategory.iOS.equals(nextName)) {
                dayContentBean.setHasIos();
            } else if (GankCategory.RECOMMEND.equals(nextName)) {
                dayContentBean.setHasRecommend();
            } else if (GankCategory.REST_VIDEO.equals(nextName)) {
                dayContentBean.setHasRestVideo();
            }
        }
        jsonReader.endArray();
    }

}
