package com.example.wuxio.gankexamples.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * @author wuxio 2018-04-29:14:01
 */
public class BackPressUtil {

    private static long exitTime = 0;


    /**
     * 提示消息
     *
     * @param activity 将要退出的Activity
     * @return : false:两次backPress间隔大于2s,true:小于2s
     */
    public static boolean showInfo(Activity activity) {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(activity.getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

}
