package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;

import java.io.IOException;
import java.io.Reader;

/**
 * @author wuxio 2018-05-02:17:15
 */
public class CategoryResultParser {

    public static CategoryResult parse(Reader reader) {

        JsonReader jsonReader = new JsonReader(reader);
        CategoryResult categoryResult = new CategoryResult();

        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {

                String name = jsonReader.nextName();

                if ("error".equals(name)) {

                    categoryResult.error = jsonReader.nextBoolean();

                } else if ("results".equals(name)) {

                    categoryResult.results = GankCategoryBeanListParser.parseArray(jsonReader);
                }
            }

            jsonReader.endObject();
            return categoryResult;

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {

                jsonReader.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return null;
    }
}
