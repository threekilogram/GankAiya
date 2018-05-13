package com.example.wuxio.gankexamples;

import java.lang.ref.WeakReference;

/**
 * @author wuxio 2018-05-07:15:09
 */
public abstract class BaseManager < T > {

    protected WeakReference< T > mReference;


    public void register(T t) {

        mReference = new WeakReference<>(t);
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
    public T get() throws NullPointerException {

        T t = mReference.get();

        /* throw a NullPointerException if t is null  */
        t.toString();

        return t;
    }


    /**
     * 对activity进行操作
     */
    public void onStart() {

    }
}
