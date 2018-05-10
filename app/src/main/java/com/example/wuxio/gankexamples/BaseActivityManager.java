package com.example.wuxio.gankexamples;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-07:15:09
 */
public abstract class BaseActivityManager < T extends Activity > {

    protected WeakReference< T > mReference;


    public void register(T activity) {

        mReference = new WeakReference<>(activity);
    }


    public void unRegister() {

        mReference.clear();
    }


    /**
     * common use try catch to call
     *
     * @return activity maybe null
     */
    @Nullable
    public T getActivity() throws NullPointerException {

        T t = mReference.get();

        /* throw a NullPointerException if t is null  */
        String s = t.toString();

        return t;
    }


    /**
     * 对activity进行操作
     */
    public void onActivityCreate() {

    }
}
