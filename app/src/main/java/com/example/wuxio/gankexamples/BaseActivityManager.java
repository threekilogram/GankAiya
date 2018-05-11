package com.example.wuxio.gankexamples;

import android.app.Activity;

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

        if (mReference != null) {
            mReference.clear();
        }
    }


    /**
     * common use try catch to call
     *
     * @return activity maybe null
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public T getActivity() throws NullPointerException {

        T t = mReference.get();

        /* throw a NullPointerException if t is null  */
        t.toString();

        return t;
    }


    /**
     * 对activity进行操作
     */
    public void onActivityCreate() {

    }
}
