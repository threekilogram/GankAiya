package com.example.wuxio.gankexamples.excutors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxio 2018-04-15:10:08
 */
public class ThreadPools {

    public static ThreadPoolExecutor sBackGroundThreadPool = new ThreadPoolExecutor(
            2,
            3,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque< Runnable >(),
            new AppThreadFactory(Thread.NORM_PRIORITY - 2)
    );

    public static ThreadPoolExecutor sNormalThreadPool = new ThreadPoolExecutor(
            1,
            1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque< Runnable >(),
            new AppThreadFactory(Thread.NORM_PRIORITY)
    );

    public static Handler callBackHandler = new Handler(Looper.getMainLooper());

    public static class AppThreadFactory implements ThreadFactory {

        private int level = Thread.NORM_PRIORITY;


        public AppThreadFactory(int level) {

            this.level = level;
        }


        @Override
        public Thread newThread(@NonNull Runnable r) {

            return new AppThread(r);
        }


        public Thread newThread(@NonNull Runnable r, int level) {

            return new AppThread(r, level);
        }
    }

    public static class AppThread extends Thread {

        private final String namePrefix = "AppThread-";
        private static int count;


        public AppThread(Runnable target) {

            super(target);
            setPriority(Thread.NORM_PRIORITY - 2);
            setName(String.format(Locale.CHINA, "%s%d", namePrefix, count++));
        }


        public AppThread(Runnable target, int level) {

            super(target);
            setPriority(level);
            setName(String.format(Locale.CHINA, "%s%d", namePrefix, count++));
        }
    }


    public static void run(Runnable runnable) {

        sBackGroundThreadPool.execute(runnable);
    }


    public static void runOnUIThread(Runnable runnable) {

        callBackHandler.post(runnable);
    }

}
