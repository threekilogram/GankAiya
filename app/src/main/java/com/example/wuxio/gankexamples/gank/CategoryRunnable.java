package com.example.wuxio.gankexamples.gank;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.constant.CategoryConstant;
import com.example.wuxio.gankexamples.dao.category.CategoryDao;
import com.example.wuxio.gankexamples.dao.factory.DaoFactory;
import com.example.wuxio.gankexamples.model.CategoryResult;
import com.example.wuxio.gankexamples.model.CategoryResultParser;
import com.example.wuxio.gankexamples.model.DaoIdUtil;
import com.example.wuxio.gankexamples.model.ResultsBean;
import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 获取分类数据,并保存到数据库
 *
 * @author wuxio
 */
public class CategoryRunnable implements Runnable {

    private static final String TAG = "CategoryRunnable";

    private String    type;
    private int       count;
    private int       page;
    private ObjectBus mBus;

    public static final String RESULTS_BEANS_BUS_KEY = "CategoryBeautyRunnable_ResultsBean_List";


    public CategoryRunnable(String type, int count, int page, ObjectBus bus) {

        this.type = type;
        this.count = count;
        this.page = page;
        this.mBus = bus;
    }


    @Override
    public void run() {

        /* 从网络读取数据,之后保存结果 */

        Call< ResponseBody > categoryData = NetWork.gankApi().getCategoryData(type, count, page);

        try {
            Response< ResponseBody > gankResponse = categoryData.execute();
            if (gankResponse.isSuccessful()) {

                /* 从服务器读取数据流 */

                Reader charStream = gankResponse.body().charStream();

                if (charStream == null) {
                    return;
                }

                /* 解析数据流为数据类 */

                CategoryResult result = CategoryResultParser.parse(charStream);

                if (result.error) {

                    // TODO: 2018-05-05 error handle

                    return;
                } else {

                    /* 将分类数据存储到数据库 */

                    List< ResultsBean > results = result.results;
                    DaoIdUtil.setResultsBeanID(results);

                    CategoryDao categoryDao = DaoFactory.getCategoryDao(CategoryConstant.BEAUTY);
                    categoryDao.insert(results);
                }

            } else {

                // TODO: 2018-05-01 获取数据失败 response failed

            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO: 2018-05-01 problem to connect to server
        }
    }
}