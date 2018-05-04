package com.example.wuxio.gankexamples;

import android.app.Application;

import com.example.objectbus.message.Messengers;
import com.example.objectbus.schedule.Scheduler;

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

        Messengers.init();
        Scheduler.init();

    }


    public BoxStore getBoxStore() {

        return mBoxStore;
    }
}
