package com.example.wuxio.gankexamples.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.example.wuxio.gankexamples.App;

import java.io.File;

/**
 * @author wuxio 2018-05-05:19:13
 */
public class FileManager {

    //============================ file ============================

    private static File appFile;
    private static File appPicFile;

    static {
        getAppFile();
    }

    public static File getAppFile() {

        if (appFile == null) {
            App app = App.INSTANCE;

            String storageState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                appFile = app.getExternalFilesDir(null);
            } else {
                appFile = app.getFilesDir();
            }
        }

        return appFile;
    }


    public static File getAppPicFile() {

        if (appPicFile == null) {

            appPicFile = new File(appFile, "pic");
        }
        return appPicFile;
    }


    public static long getCachedGankDay() {

        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(
                "cached_gank_day",
                Context.MODE_PRIVATE
        );

        return preferences.getLong("day", -1);
    }


    public static void putCachedGankDay(long day) {

        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(
                "cached_gank_day",
                Context.MODE_PRIVATE
        );

        preferences.edit().putLong("day", day).apply();
    }
}
