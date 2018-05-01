package com.example.wuxio.gankexamples.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.Random;

/**
 * @author wuxio 2018-05-01:20:16
 *
 * 用于发送一个通知,主要用于后台任务处理完成之后,继续下一步任务,使用handler 1.解决方法栈过长,2.可以切换到主线程
 */
public class Messengers {

    private static SendHandler sendHandler;
    private static SendHandler mainHandler;

    private static final Random RANDOM = new Random();

    static {
        HandlerThread thread = new HandlerThread("Messengers");
        thread.start();
        sendHandler = new SendHandler(thread.getLooper());
        mainHandler = new SendHandler(Looper.getMainLooper());
    }

    /**
     * 发送一条空白消息
     *
     * @param what 标识,如果时单数,发送到主线程,如果时复数,发送到后台线程处理
     * @param who  发送给谁
     */
    public static void send(int what, @NonNull OnMessageReceiveListener who) {

        send(what, null, who);
    }


    /**
     * 发送一条空白消息,携带一个数据
     *
     * @param what 标识,如果时单数,发送到主线程,如果时复数,发送到后台线程处理
     * @param who  发送给谁
     */
    public static void send(int what, Object extra, @NonNull OnMessageReceiveListener who) {

        int key = RANDOM.nextInt();

        final int judge = 2;

        Message obtain = Message.obtain();
        obtain.what = what;
        obtain.arg1 = key;

        if (what % judge == 0) {
            sendHandler.MESSAGE_HOLDER_ARRAY.put(key, new Holder(what, extra, who));
            sendHandler.sendMessage(obtain);
        } else {
            mainHandler.MESSAGE_HOLDER_ARRAY.put(key, new Holder(what, extra, who));
            mainHandler.sendMessage(obtain);
        }
    }

    //============================ send messenger async ============================

    /**
     * 发送消息的handler
     */
    private static class SendHandler extends Handler {

        private final SparseArray< Holder > MESSAGE_HOLDER_ARRAY = new SparseArray<>();


        /**
         * 不同的loop 消息送到不同的线程
         *
         * @param looper looper
         */
        private SendHandler(Looper looper) {

            super(looper);
        }


        /**
         * 处理消息
         */
        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;

            SparseArray< Holder > holderArray = MESSAGE_HOLDER_ARRAY;

            /* 遍历Holder, 找出需要处理的holder*/

            int size = holderArray.size();
            for (int i = 0; i < size; i++) {

                int key = holderArray.keyAt(i);
                Holder holder = holderArray.get(key);

                if (holder.what == what && key == msg.arg1) {

                    Object extra = holder.extra;
                    OnMessageReceiveListener listener = holder.listener;

                    if (extra == null) {

                        listener.onReceive(what);
                    } else {

                        listener.onReceive(what, extra);
                    }
                    MESSAGE_HOLDER_ARRAY.delete(key);
                    break;
                }
            }
        }
    }

    //============================ holder ============================

    /**
     * 记录message信息
     */
    private static class Holder {

        private int                      what;
        private Object                   extra;
        private OnMessageReceiveListener listener;


        public Holder(int what, Object extra, OnMessageReceiveListener listener) {

            this.what = what;
            this.extra = extra;
            this.listener = listener;
        }
    }
}
