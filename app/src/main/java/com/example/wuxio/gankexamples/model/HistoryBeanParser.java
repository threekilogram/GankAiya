package com.example.wuxio.gankexamples.model;

import android.util.JsonReader;

import com.example.wuxio.gankexamples.utils.date.DateFormat;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-09:21:29
 */
public class HistoryBeanParser {

    public static List< HistoryBean > parse(Reader reader) {

        JsonReader jsonReader = new JsonReader(reader);

        ArrayList< HistoryBean > historyBeans = new ArrayList<>();

        try {
            jsonReader.beginObject();
            String name = jsonReader.nextName();

            final String firstValueName = "error";

            if (firstValueName.equals(name)) {

                boolean isError = jsonReader.nextBoolean();
                if (!isError) {

                    name = jsonReader.nextName();
                    final String secondValueName = "results";
                    if (secondValueName.equals(name)) {

                        jsonReader.beginArray();

                        while (jsonReader.hasNext()) {
                            String dateString = jsonReader.nextString();
                            long ymd = DateFormat.formatYMD(dateString);
                            HistoryBean bean = new HistoryBean();
                            bean.date = ymd;
                            historyBeans.add(bean);
                        }

                        jsonReader.endArray();
                    }
                }

            }
            jsonReader.endObject();

            return historyBeans;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
