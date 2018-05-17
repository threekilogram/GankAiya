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

    private final int mCount = 20;


    private ModelManager() {

    }


    public static ModelManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final ModelManager INSTANCE = new ModelManager();
    }

    //============================ load ============================


    private void load(String category, int count, int page, List< GankCategoryBean > beanList) {

        GankCategoryApi categoryApi = RetrofitManger.categoryApi();
        Call< ResponseBody > categoryData = categoryApi.getCategoryData(category, count, page);

        try {
            Response< ResponseBody > response = categoryData.execute();
            if (response.isSuccessful()) {

                Reader reader = response.body().charStream();
                CategoryResult categoryResult = CategoryResultParser.parse(reader);
                beanList.addAll(categoryResult.results);

            } else {

                Log.e(TAG, "load: response is not Successful");
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, "load: cant connect to server");
        }
    }

    //============================ load category beauty ============================

    private List< GankCategoryBean > mCategoryBeauties;


    /**
     * 每调用一次,增加5个数据
     */
    public List< GankCategoryBean > loadBeauty() {

        final int count = mCount;

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

    //============================ total ============================


    public List< GankCategoryBean > loadCategory(String category) {

        switch (category) {

            case GankCategory.Android:

                if (mCategoryAndroids == null || mCategoryAndroids.size() < 20) {
                    return loadAndroid();
                } else {
                    return mCategoryAndroids;
                }

            case GankCategory.App:

                if (mCategoryApps == null || mCategoryApps.size() < 20) {
                    return loadApp();
                } else {
                    return mCategoryApps;
                }

            case GankCategory.iOS:

                if (mCategoryIOSs == null || mCategoryIOSs.size() < 20) {
                    return loadIOS();
                } else {
                    return mCategoryIOSs;
                }

            case GankCategory.FRONT:

                if (mCategoryFront == null || mCategoryFront.size() < 20) {
                    return loadFront();
                } else {
                    return mCategoryFront;
                }

            case GankCategory.RECOMMEND:

                if (mCategoryRecommend == null || mCategoryRecommend.size() < 20) {
                    return loadRecommend();
                } else {
                    return mCategoryRecommend;
                }

            case GankCategory.EXTRA_RESOURCES:

                if (mCategoryExtra == null || mCategoryExtra.size() < 20) {
                    return loadExtra();
                } else {
                    return mCategoryExtra;
                }

            case GankCategory.REST_VIDEO:

                if (mCategoryRest == null || mCategoryRest.size() < 20) {
                    return loadRest();
                } else {
                    return mCategoryRest;
                }

            default:
                break;
        }

        return loadAndroid();
    }


    public List< GankCategoryBean > loadCategoryMore(String category) {

        switch (category) {

            case GankCategory.Android:

                return loadAndroid();

            case GankCategory.App:

                return loadApp();

            case GankCategory.iOS:

                return loadIOS();

            case GankCategory.FRONT:

                return loadFront();

            case GankCategory.RECOMMEND:

                return loadRecommend();

            case GankCategory.EXTRA_RESOURCES:

                return loadExtra();

            case GankCategory.REST_VIDEO:

                return loadRest();

            default:
                break;
        }

        return loadAndroid();
    }

    //============================ load category Android ============================

    private List< GankCategoryBean > mCategoryAndroids;


    public List< GankCategoryBean > loadAndroid() {

        if (mCategoryAndroids == null) {
            mCategoryAndroids = new ArrayList<>(mCount);
        }

        int page = mCategoryAndroids.size() / mCount + 1;

        load(GankCategory.Android, mCount, page, mCategoryAndroids);

        return mCategoryAndroids;
    }

    //============================ load category App ============================

    private List< GankCategoryBean > mCategoryApps;


    public List< GankCategoryBean > loadApp() {

        mCategoryApps = mLoadAction.load(GankCategory.App, mCategoryApps);
        return mCategoryApps;
    }

    //============================ load category App ============================

    private List< GankCategoryBean > mCategoryIOSs;


    public List< GankCategoryBean > loadIOS() {

        mCategoryIOSs = mLoadAction.load(GankCategory.iOS, mCategoryIOSs);
        return mCategoryIOSs;
    }

    //============================ load category front ============================

    private List< GankCategoryBean > mCategoryFront;


    public List< GankCategoryBean > loadFront() {

        mCategoryFront = mLoadAction.load(GankCategory.FRONT, mCategoryFront);
        return mCategoryFront;
    }

    //============================ load category recommend ============================

    private List< GankCategoryBean > mCategoryRecommend;


    public List< GankCategoryBean > loadRecommend() {

        mCategoryRecommend = mLoadAction.load(GankCategory.RECOMMEND, mCategoryRecommend);
        return mCategoryRecommend;
    }

    //============================ load category extra ============================

    private List< GankCategoryBean > mCategoryExtra;


    public List< GankCategoryBean > loadExtra() {

        mCategoryExtra = mLoadAction.load(GankCategory.EXTRA_RESOURCES, mCategoryExtra);
        return mCategoryExtra;
    }

    //============================ load category video ============================

    private List< GankCategoryBean > mCategoryRest;


    public List< GankCategoryBean > loadRest() {

        mCategoryRest = mLoadAction.load(GankCategory.REST_VIDEO, mCategoryRest);
        return mCategoryRest;
    }

    //============================ 简化编程 ============================

    private interface LoadAction {

        /**
         * 简化编程
         *
         * @param category             category
         * @param gankCategoryBeanList result
         */
        List< GankCategoryBean > load(String category, List< GankCategoryBean > gankCategoryBeanList);

    }

    private LoadAction mLoadAction = new LoadAction() {
        @Override
        public List< GankCategoryBean > load(String category, List< GankCategoryBean > gankCategoryBeanList) {

            final int count = mCount;

            if (gankCategoryBeanList == null) {
                gankCategoryBeanList = new ArrayList<>(count);
            }

            int page = gankCategoryBeanList.size() / count + 1;

            ModelManager.this.load(category, count, page, gankCategoryBeanList);

            return gankCategoryBeanList;
        }
    };
}
