package com.example.wuxio.gankexamples.dao.factory;

import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.dao.image.ImageBeanDao;
import com.example.wuxio.gankexamples.model.ImageBean;
import com.example.wuxio.gankexamples.utils.Url2Long;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

/**
 * @author wuxio 2018-05-05:10:46
 */
public class ImageBeanDaoImpl implements ImageBeanDao {

    //============================ singleTon ============================


    private ImageBeanDaoImpl() {

        mImageBeanBox = App.INSTANCE.getBoxStore().boxFor(ImageBean.class);
    }


    public static ImageBeanDaoImpl getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final ImageBeanDaoImpl INSTANCE = new ImageBeanDaoImpl();
    }

    //============================ crud ============================

    private Box< ImageBean > mImageBeanBox;


    @Override
    public ImageBean query(String url) {

        long id = Url2Long.to(url);
        return mImageBeanBox.get(id);
    }


    @Override
    public List< ImageBean > query(List< String > urls) {

        final int length = urls.size();
        long[] ids = new long[length];
        for (int i = 0; i < length; i++) {
            ids[i] = Url2Long.to(urls.get(i));
        }

        return mImageBeanBox.get(ids);
    }


    @Override
    public void insert(List< String > urls) {

        final int size = urls.size();
        final ArrayList< ImageBean > imageBeans = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {

            ImageBean imageBean = new ImageBean();
            String url = urls.get(i);
            imageBean.url = url;
            imageBean.id = Url2Long.to(url);
            imageBeans.add(imageBean);
        }

        mImageBeanBox.put(imageBeans);
    }
}
