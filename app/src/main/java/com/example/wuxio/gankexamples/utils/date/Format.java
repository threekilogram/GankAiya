package com.example.wuxio.gankexamples.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author wuxio 2018-05-02:16:27
 */
public class Format {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            Locale.CHINA
    );


    public static long format(String date) {

        try {
            Date parse = DATE_FORMAT.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }
}
