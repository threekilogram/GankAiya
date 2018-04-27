package com.example.wuxio.gankexamples.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wuxio 2018-04-27:13:40
 */
public class ViewSupplier {

    public static < T extends View > T get(Context context, ViewGroup root, @LayoutRes int layoutRes) {

        return (T) LayoutInflater.from(context).inflate(layoutRes, root, false);
    }
}
