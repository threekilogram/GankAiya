package com.example.wuxio.gankexamples.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wuxio 2018-05-05:10:18
 */
public class MD5 {

    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String make(String string) {

        if (TextUtils.isEmpty(string)) {
            return "";
        }

        byte[] bytes = md5.digest(string.getBytes());

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            String temp = Integer.toHexString(b & 0xff);
            if (temp.length() == 1) {
                stringBuilder.append(0).append(temp);
            } else {
                stringBuilder.append(temp);
            }

        }
        return stringBuilder.toString();
    }

}
