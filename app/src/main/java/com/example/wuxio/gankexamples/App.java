package com.example.wuxio.gankexamples;

import android.app.Application;

import com.example.wuxio.gankexamples.async.Scheduler;
import com.example.wuxio.gankexamples.beauty.model.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * @author wuxio 2018-04-15:9:53
 */
public class App extends Application {

    public static App INSTANCE;

    public BoxStore mBoxStore;


    @Override
    public void onCreate() {

        super.onCreate();
        INSTANCE = this;

        Scheduler.init();

        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();
    }


    public BoxStore getBoxStore() {

        return mBoxStore;
    }
}
