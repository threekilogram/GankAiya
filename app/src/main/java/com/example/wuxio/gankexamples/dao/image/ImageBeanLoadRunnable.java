package com.example.wuxio.gankexamples.dao.image;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.model.ImageBean;

import java.util.List;

/**
 * @author wuxio 2018-05-05:11:16
 */
public class ImageBeanLoadRunnable implements Runnable {

    private ObjectBus mBus;


    public ImageBeanLoadRunnable(ObjectBus bus) {

        mBus = bus;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void run() {

        List< ImageBean > imageBeans = (List< ImageBean >) mBus.off(ImageBeanQueryRunnable.BUS_KEY_BEANS);

        int size = imageBeans.size();
        for (int i = 0; i < size; i++) {
            ImageBean bean = imageBeans.get(i);
        }
    }
}
