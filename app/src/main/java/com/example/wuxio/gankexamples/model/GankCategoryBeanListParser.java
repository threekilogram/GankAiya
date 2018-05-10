package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-04:21:25
 */
public class GankCategoryBeanListParser {

    public static List< GankCategoryBean > parseArray(JsonReader jsonReader) {

        List< GankCategoryBean > results = new ArrayList<>();

        try {

            jsonReader.beginArray();

            while (jsonReader.hasNext()) {

                GankCategoryBean bean = GankCategoryBeanParser.parseCategory(jsonReader);
                results.add(bean);
            }

            jsonReader.endArray();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return results;
    }

}

