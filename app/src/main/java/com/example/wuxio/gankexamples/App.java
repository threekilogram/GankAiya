package com.example.wuxio.gankexamples;

import android.app.Application;

/**
 * @author wuxio 2018-04-15:9:53
 */
public class App extends Application {

    public static App INSTANCE;


    @Override
    public void onCreate() {

        super.onCreate();
        INSTANCE = this;
    }
}
