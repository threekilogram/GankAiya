package com.example.wuxio.gankexamples.file;

/**
 * @author wuxio 2018-05-05:19:37
 */
public class FileNameUtils {

    public static String makeName(String url) {

        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex);
    }

}
