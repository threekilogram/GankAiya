package com.example.wuxio.gankexamples.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author wuxio 2018-05-02:16:27
 */
public class DateFormat {

    public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            Locale.CHINA
    );

    public static final SimpleDateFormat YMD_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.CHINA
    );


    public static long formatFull(String date) {

        try {
            Date parse = FULL_DATE_FORMAT.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }

    public static long formatYMD(String date) {

        try {
            Date parse = YMD_DATE_FORMAT.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }

}
