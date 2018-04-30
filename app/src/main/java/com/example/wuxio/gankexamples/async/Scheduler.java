package com.example.wuxio.gankexamples.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;

/**
 * @author wuxio 2018-04-30:0:54
 */
public class Scheduler extends HandlerThread {

    private static ScheduleHandler sHandler;
    private static final transient ArrayList< Runnable > RUNNABLE       = new ArrayList<>();
    private static final transient ArrayList< Runnable > DELAY_RUNNABLE = new ArrayList<>();


    private Scheduler() {

        super("Scheduler-Thread");
    }


    /**
     * 初始化
     */
    public static void init() {

        Scheduler schedule = getInstance();
        schedule.start();
        sHandler = new ScheduleHandler(schedule.getLooper());
    }


    /**
     * do something in background
     *
     * @param runnable something
     */
    public static void todo(Runnable runnable) {

        RUNNABLE.add(runnable);
        sHandler.sendEmptyMessage(0);
    }


    /**
     * do something in background
     *
     * @param runnable something
     */
    public static void todo(Runnable runnable, int delayed) {

        if (delayed <= 0) {
            todo(runnable);
            return;
        }

        Message obtain = Message.obtain();
        long time = System.currentTimeMillis() + delayed;
        obtain.arg1 = (int) (time >> 32);
        obtain.arg2 = (int) time;
        obtain.obj = runnable.toString();

        DELAY_RUNNABLE.add(runnable);

        obtain.what = 1;
        sHandler.sendMessageDelayed(obtain, delayed);
    }

    //============================ send task on self thread ============================

    private static class ScheduleHandler extends Handler {

        private static final String TAG = "ScheduleHandler";


        ScheduleHandler(Looper looper) {

            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {

                /* send task to executor */

                while (RUNNABLE.size() > 0) {
                    Runnable runnable = RUNNABLE.get(0);
                    AppExecutor.execute(runnable);
                    RUNNABLE.remove(0);
                }
                return;
            }

            if (msg.what == 1) {

                long currentTimeMillis = System.currentTimeMillis();

                int size = DELAY_RUNNABLE.size();
                for (int i = 0; i < size; i++) {

                    Runnable runnable = DELAY_RUNNABLE.get(i);

                    if (!msg.obj.equals(runnable.toString())) {
                        continue;
                    }

                    long src = msg.arg1;
                    src = (src << 32) | msg.arg2;
                    if (src <= currentTimeMillis) {
                        AppExecutor.execute(runnable);
                        DELAY_RUNNABLE.remove(i);
                        return;
                    }
                }
            }
        }
    }

    //============================ singleTon ============================


    private static Scheduler getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final Scheduler INSTANCE = new Scheduler();
    }
}
