package com.example.wuxio.gankexamples.model;

import android.util.Log;

import com.example.wuxio.gankexamples.constant.GankCategory;
import com.example.wuxio.gankexamples.model.net.GankCategoryApi;
import com.example.wuxio.gankexamples.retrofit.RetrofitManger;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-10:21:49
 */
public class ModelManager {

    private static final String TAG = "ModelManager";

    //============================ singleTon ============================


    private ModelManager() {

    }


    public static ModelManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final ModelManager INSTANCE = new ModelManager();
    }

    //============================ load categoryData ============================

    private List< GankCategoryBean > mCategoryBeauties;


    private void load(String category, int count, int page, List< GankCategoryBean > beanList) {

        GankCategoryApi categoryApi = RetrofitManger.categoryApi();
        Call< ResponseBody > categoryData = categoryApi.getCategoryData(category, count, page);

        try {
            Response< ResponseBody > response = categoryData.execute();
            if (response.isSuccessful()) {

                Reader reader = response.body().charStream();
                CategoryResult categoryResult = CategoryResultParser.parse(reader);

                if (beanList == null) {
                    beanList = new ArrayList<>();
                }

                beanList.addAll(categoryResult.results);
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, "load: cant connect to server");
        }
    }


    /**
     * 每调用一次,增加5个数据
     */
    public List< GankCategoryBean > loadBeauty() {

        final int count = 5;

        if (mCategoryBeauties == null) {
            mCategoryBeauties = new ArrayList<>(count);
        }

        int page = mCategoryBeauties.size() / count + 1;
        load(GankCategory.BEAUTY, count, page, mCategoryBeauties);

        return mCategoryBeauties;
    }


    public List< GankCategoryBean > getCategoryBeauties() {

        return mCategoryBeauties;
    }


    public List< GankCategoryBean > loadMoreBeauty() {

        return loadBeauty();
    }
}
