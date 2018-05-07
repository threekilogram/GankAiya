package com.example.wuxio.gankexamples;

import android.app.Application;

import com.example.objectbus.BusConfig;
import com.example.wuxio.gankexamples.model.MyObjectBox;
import com.squareup.leakcanary.LeakCanary;

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

        BusConfig.init();

        mBoxStore = MyObjectBox.builder().androidContext(this).build();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    public BoxStore getBoxStore() {

        return mBoxStore;
    }
}
