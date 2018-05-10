package com.example.wuxio.gankexamples.file;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wuxio.gankexamples.app.App;

/**
 * @author wuxio 2018-05-10:9:33
 */
public class PreferenceManager {

    public static long getAlreadyCachedGankDay() {

        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(
                "cached_gank_day",
                Context.MODE_PRIVATE
        );

        return preferences.getLong("day", -1);
    }


    public static void putAlreadyCachedGankDay(long day) {

        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(
                "cached_gank_day",
                Context.MODE_PRIVATE
        );

        preferences.edit().putLong("day", day).apply();
    }

}
