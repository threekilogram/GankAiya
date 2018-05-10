package com.example.wuxio.gankexamples.app;

import android.app.Application;

import com.example.objectbus.BusConfig;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author wuxio 2018-04-15:9:53
 */
public class App extends Application {

    public static App INSTANCE;


    @Override
    public void onCreate() {

        super.onCreate();
        INSTANCE = this;

        /* ObjectBus */
        BusConfig.init();


        /* LeakCanary */
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }
}
