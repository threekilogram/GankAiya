package com.example.wuxio.gankexamples.utils;

/**
 * @author wuxio 2018-05-05:10:36
 */
public class Url2Long {

    /**
     * to string to long
     *
     * @param url url
     * @return long
     */
    public static long to(String url) {

        final int length = url.length();

        long id = 0;
        for (int i = 0; i < length; i++) {
            int j = url.charAt(i);
            id = 31 * id + j;
        }

        return id;
    }
}
