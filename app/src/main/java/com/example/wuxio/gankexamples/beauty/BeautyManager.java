package com.example.wuxio.gankexamples.beauty;

import android.util.Log;

import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.async.Messengers;
import com.example.wuxio.gankexamples.async.OnMessageReceiveListener;
import com.example.wuxio.gankexamples.async.Scheduler;
import com.example.wuxio.gankexamples.beauty.model.BeautyEntity;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * @author wuxio 2018-05-02:15:28
 */
public class BeautyManager implements OnMessageReceiveListener {

    private static final String TAG = "BeautyManager";
    private final Box< BeautyEntity > mBeautyEntityBox;


    public void getBeauty(int count, int page) {

        Scheduler.todo(new BeautyJsonAction(count, page));
    }

    //============================ message ============================


    public void notifyNew(List< BeautyEntity > newBeauties) {

        Messengers.send(12, newBeauties, this);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(int what, Object extra) {

        if (what == 12) {
            List< BeautyEntity > entities = (List< BeautyEntity >) extra;
            int size = entities.size();
            Log.i(TAG, "onReceive:" + size);

            for (int i = 0; i < size; i++) {
                BeautyEntity entity = entities.get(i);
                long date = entity.publishDate;
                BeautyEntity beautyEntity = mBeautyEntityBox.get(date);
                if (beautyEntity == null) {
                    mBeautyEntityBox.put(entity);
                }
            }
        }
    }

    //============================ singleTon ============================


    private BeautyManager() {

        BoxStore store = App.INSTANCE.getBoxStore();
        mBeautyEntityBox = store.boxFor(BeautyEntity.class);
    }


    public static BeautyManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final BeautyManager INSTANCE = new BeautyManager();
    }
}
