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

        mReference.clear();
    }


    public void onActivityCreate() {

    }


    public void onActivityDestroyed() {

    }

}
